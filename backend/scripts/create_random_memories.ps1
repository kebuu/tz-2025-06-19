# Script to create random memories for all users
# Each user will have between 0 and 20 memories
# Some memories will be linked to one or two users

# Function to generate a random date in the past
function Get-RandomPastDate {
    $daysInPast = Get-Random -Minimum 1 -Maximum 365
    return (Get-Date).AddDays(-$daysInPast).ToString("yyyy-MM-dd")
}

# Function to generate random memory text
function Get-RandomMemoryText {
    $memoryTexts = @(
        "Had a great time at the beach today!",
        "Visited the museum and learned so much about history.",
        "Cooked a delicious dinner with friends.",
        "Went hiking in the mountains and saw amazing views.",
        "Attended a concert and it was incredible!",
        "Finished reading a fascinating book.",
        "Had a productive day working on my project.",
        "Celebrated a friend's birthday with a surprise party.",
        "Tried a new restaurant and the food was amazing.",
        "Went to the movies and watched an interesting film.",
        "Spent the day gardening and planting new flowers.",
        "Visited family and had a wonderful time catching up.",
        "Went for a long walk in the park and enjoyed nature.",
        "Attended a workshop and learned new skills.",
        "Had a relaxing day at home watching my favorite shows.",
        "Went shopping and found some great deals.",
        "Played board games with friends and had a lot of fun.",
        "Tried a new recipe and it turned out delicious.",
        "Went to a coffee shop and enjoyed a peaceful afternoon.",
        "Attended a yoga class and felt very refreshed afterward."
    )

    $randomIndex = Get-Random -Minimum 0 -Maximum $memoryTexts.Count
    return $memoryTexts[$randomIndex]
}

# Function to create a memory for a user
function Create-Memory {
    param (
        [Parameter(Mandatory=$true)]
        [string]$userId,

        [Parameter(Mandatory=$true)]
        [string]$text,

        [Parameter(Mandatory=$true)]
        [string]$day,

        [Parameter(Mandatory=$false)]
        [string]$userPseudo = ""
    )

    # Create memory JSON payload
    $memoryJson = @{
        text = $text
        day = $day
    } | ConvertTo-Json

    # Set up Basic authentication with user ID as username and "zenika" as password
    $base64Auth = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("$($userId):zenika"))
    $headers = @{
        Authorization = "Basic $base64Auth"
    }

    if ($userPseudo -ne "") {
        Write-Host "  Creating memory for user $userPseudo with text: $text"
    }

    # Send the POST request to create the memory
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8080/api/memories" -Method POST -Body $memoryJson -ContentType "application/json" -Headers $headers

        # Extract the memory ID from the Location header
        $locationHeader = $response.Headers.Location
        if ($locationHeader) {
            $memoryId = $locationHeader.Split('/')[-1]

            if ($userPseudo -ne "") {
                Write-Host "    Memory created with ID: $memoryId"
            }

            return @{
                id = $memoryId
                ownerId = $userId
                text = $text
                day = $day
            }
        }
    }
    catch {
        if ($userPseudo -ne "") {
            Write-Host "    Error creating memory: $_"
        }
        return $null
    }
}

# Get all users
Write-Host "Fetching all users..."
$allUsers = Invoke-WebRequest -Uri "http://localhost:8080/api/users" -Method GET | Select-Object -ExpandProperty Content | ConvertFrom-Json

# Track created memories
$createdMemories = @()

# Create random memories for each user
foreach ($user in $allUsers) {
    $userId = $user.id
    $userPseudo = $user.pseudo

    # Determine how many memories to create for this user (0-20)
    $memoryCount = Get-Random -Minimum 0 -Maximum 21

    Write-Host "Creating $memoryCount memories for user $userPseudo (ID: $userId)"

    # Create the specified number of memories
    for ($i = 0; $i -lt $memoryCount; $i++) {
        $memoryText = Get-RandomMemoryText
        $memoryDay = Get-RandomPastDate

        $memory = Create-Memory -userId $userId -text $memoryText -day $memoryDay -userPseudo $userPseudo
        if ($memory -ne $null) {
            $createdMemories += $memory
        }
    }
}

# Create shared memories (memories with the same text and date for multiple users)
Write-Host "`nCreating shared memories between users..."

# Determine how many shared memories to create
$sharedMemoryCount = [Math]::Min(10, $allUsers.Count)

for ($i = 0; $i -lt $sharedMemoryCount; $i++) {
    # Select 2 random users
    $userIndices = Get-Random -InputObject (0..($allUsers.Count-1)) -Count ([Math]::Min(2, $allUsers.Count))
    $selectedUsers = $userIndices | ForEach-Object { $allUsers[$_] }

    # Create a shared memory text and date
    $sharedMemoryText = "Shared memory: $(Get-RandomMemoryText)"
    $sharedMemoryDay = Get-RandomPastDate

    Write-Host "Creating shared memory between users: $($selectedUsers.pseudo -join ', ')"

    # Create the memory for each selected user
    foreach ($user in $selectedUsers) {
        $memory = Create-Memory -userId $user.id -text $sharedMemoryText -day $sharedMemoryDay -userPseudo $user.pseudo
        if ($memory -ne $null) {
            $createdMemories += $memory
        }
    }
}

Write-Host "`nCreated a total of $($createdMemories.Count) memories"

# Summary of memories created per user
$memoriesByUser = $createdMemories | Group-Object -Property ownerId
Write-Host "`nMemories created per user:"
foreach ($group in $memoriesByUser) {
    $userPseudo = ($allUsers | Where-Object { $_.id -eq $group.Name }).pseudo
    Write-Host "User $userPseudo (ID: $($group.Name)): $($group.Count) memories"
}

# Summary of shared memories
Write-Host "`nShared memories (same text for multiple users):"
$sharedMemories = $createdMemories | Group-Object -Property text | Where-Object { $_.Count -gt 1 }
foreach ($group in $sharedMemories) {
    $users = $group.Group | ForEach-Object {
        $userId = $_.ownerId
        $userPseudo = ($allUsers | Where-Object { $_.id -eq $userId }).pseudo
        "$userPseudo (ID: $userId)"
    }
    Write-Host "Memory text: $($group.Name)"
    Write-Host "Shared between: $($users -join ', ')"
    Write-Host ""
}
