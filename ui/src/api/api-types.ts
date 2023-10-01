export interface UserResponse {
    id: number,
    roles: Role[]
}

export interface Role {
    id: number,
    name: string
}
