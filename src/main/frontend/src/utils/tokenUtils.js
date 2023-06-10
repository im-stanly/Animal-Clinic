export function decodeRoleFromToken(token) {
  try {
    const tokenParts = token.split('.');
    const payload = JSON.parse(atob(tokenParts[1]));

    return payload.role;
  } catch (error) {
    console.error('Token decoding error:', error);
    return null;
  }
}
