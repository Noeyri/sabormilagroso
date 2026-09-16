// Sabor Milagroso - Mapa de ubicación fija (página Contacto)
// Leaflet + OpenStreetMap. Sin Google Maps, sin API key, sin facturación.
document.addEventListener('DOMContentLoaded', function () {
  var contenedor = document.getElementById('mapa-contacto');
  if (!contenedor || typeof L === 'undefined') return;

  // TODO: ajustar estas coordenadas a la ubicación EXACTA del local si se dispone
  // de un valor más preciso (ej. usando https://nominatim.openstreetmap.org
  // o marcando el punto manualmente en https://www.openstreetmap.org).
  var UBICACION_RESTAURANTE = {
    lat: -14.0678,
    lng: -75.7286,
    direccion: 'San Joaquín Viejo S-17, Ica, Perú'
  };

  var mapa = L.map(contenedor, {
    center: [UBICACION_RESTAURANTE.lat, UBICACION_RESTAURANTE.lng],
    zoom: 15,
    scrollWheelZoom: false
  });

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
    maxZoom: 19
  }).addTo(mapa);

  // Marcador fijo: no draggable (no se puede mover)
  L.marker([UBICACION_RESTAURANTE.lat, UBICACION_RESTAURANTE.lng], { draggable: false })
    .addTo(mapa)
    .bindPopup('<strong>Sabor Milagroso</strong><br/>' + UBICACION_RESTAURANTE.direccion)
    .openPopup();
});