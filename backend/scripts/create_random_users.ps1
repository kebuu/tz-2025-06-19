# Script to create 10 random users with valid emails and funny nicknames

# List to store the IDs of created users
$createdUserIds = @()

# Funny nicknames
$funnyNicknames = @(
    "BubbleTea",
    "PixelPal",
    "GiggleBot",
    "NoodleNinja",
    "QuackMaster",
    "FizzBuzz",
    "MoonWalker",
    "TacoTitan",
    "WaffleWizard",
    "ZoomZoom",
    "SnackAttack",
    "DoodleDude",
    "JellyJumper",
    "PizzaPal",
    "SillyGoose"
)

# Create 10 random users
for ($i = 0; $i -lt 10; $i++) {
    # Generate a random nickname from the list
    $randomIndex = Get-Random -Minimum 0 -Maximum $funnyNicknames.Count
    $nickname = $funnyNicknames[$randomIndex]
    
    # Add a random number to ensure uniqueness
    $randomNum = Get-Random -Minimum 100 -Maximum 999
    $nickname = "$nickname$randomNum"
    
    # Generate a random valid email
    $randomString = -join ((65..90) + (97..122) | Get-Random -Count 8 | ForEach-Object {[char]$_})
    $email = "$randomString@example.com"
    
    # Create the user JSON payload
    $userJson = @{
        email = $email
        pseudo = $nickname
    } | ConvertTo-Json
    
    Write-Host "Creating user with nickname: $nickname and email: $email"
    
    # Send the POST request to create the user
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8080/api/users" -Method POST -Body $userJson -ContentType "application/json"
        
        # Extract the user ID from the response
        $user = $response.Content | ConvertFrom-Json
        $createdUserIds += $user.id
        
        Write-Host "User created with ID: $($user.id)"
    }
    catch {
        Write-Host "Error creating user: $_"
    }
}

# Get the list of all users
$allUsers = Invoke-WebRequest -Uri "http://localhost:8080/api/users" -Method GET | Select-Object -ExpandProperty Content | ConvertFrom-Json

# Separate existing users from newly created ones
$existingUsers = $allUsers | Where-Object { $createdUserIds -notcontains $_.id }

# Output the results
Write-Host "`nCreated Users (IDs):"
$createdUserIds | ForEach-Object { Write-Host $_ }

Write-Host "`nExisting Users:"
$existingUsers | ForEach-Object { Write-Host "ID: $($_.id), Pseudo: $($_.pseudo), Email: $($_.email)" }
