export type AppUser = {
    id: string,
    name: string,
    role: "USER" | "ADMIN",
    avatarUrl: string
}