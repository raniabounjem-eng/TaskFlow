// environment.prod.ts — production (servi par Docker/Nginx)
export const environment = {
  production: true,
  apiUrl: '/api'     // chemin relatif → proxy Nginx vers le backend
};
