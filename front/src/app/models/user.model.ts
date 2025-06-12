export interface User {
  id: {
    value: string;
  };
  email: {
    value: string;
  };
  pseudo: {
    value: string;
  };
}

export interface CreateUserRequest {
  email: string;
  pseudo: string;
}