// Sabor Milagroso - Mapa interactivo para seleccionar la ubicación del cliente
// Leaflet + OpenStreetMap. Sin Google Maps, sin API key, sin facturación.
// Búsqueda de direcciones mediante Nominatim (servicio gratuito de OpenStreetMap).
document.addEventListener('DOMContentLoaded', function () {
  var modal = document.getElementById('modal-direccion');
  var btnAbrir = document.getElementById('btn-abrir-modal-direccion');
  var btnCerrar = document.getElementById('btn-cerrar-modal-direccion');
  var contenedorMapa = document.getElementById('mapa-direccion');
  var inputLat = document.getElementById('input-latitud');
  var inputLng = document.getElementById('input-longitud');
  var buscador = document.getElementById('buscador-ubicacion');
  var btnBuscar = document.getElementById('btn-buscar-ubicacion');
  var mensajeBuscador = document.getElementById('mensaje-buscador');

  if (!modal || !btnAbrir || typeof L === 'undefined') return;

  // Centro inicial: Ica, Perú
  var CENTRO_ICA = { lat: -14.0678, lng: -75.7286 };

  var mapa = null;
  var marcador = null;

  function actualizarCoordenadas(lat, lng) {
    inputLat.value = lat.toFixed(7);
    inputLng.value = lng.toFixed(7);
  }

  function inicializarMapa() {
    if (mapa) return; // ya inicializado, evita crear el mapa más de una vez

    mapa = L.map(contenedorMapa).setView([CENTRO_ICA.lat, CENTRO_ICA.lng], 14);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
      maxZoom: 19
    }).addTo(mapa);

    // Marcador arrastrable: el cliente elige su ubicación moviéndolo
    marcador = L.marker([CENTRO_ICA.lat, CENTRO_ICA.lng], { draggable: true }).addTo(mapa);

    marcador.on('dragend', function () {
      var pos = marcador.getLatLng();
      actualizarCoordenadas(pos.lat, pos.lng);
    });

    // También permite hacer clic en el mapa para reubicar el marcador
    mapa.on('click', function (e) {
      marcador.setLatLng(e.latlng);
      actualizarCoordenadas(e.latlng.lat, e.latlng.lng);
    });

    // Guarda las coordenadas iniciales (centro de Ica) por defecto
    actualizarCoordenadas(CENTRO_ICA.lat, CENTRO_ICA.lng);
  }

  function abrirModal() {
    modal.classList.remove('hidden');
    inicializarMapa();
    // El mapa se inicializa mientras el modal estaba oculto (0px de alto),
    // por lo que hay que recalcular su tamaño al mostrarlo.
    setTimeout(function () {
      mapa.invalidateSize();
    }, 50);
  }

  function cerrarModal() {
    modal.classList.add('hidden');
  }

  btnAbrir.addEventListener('click', abrirModal);
  if (btnCerrar) btnCerrar.addEventListener('click', cerrarModal);
  modal.addEventListener('click', function (e) {
    if (e.target === modal) cerrarModal();
  });

  // Búsqueda de direcciones gratuita, sin API key, usando Nominatim (OpenStreetMap)
  if (btnBuscar) {
    btnBuscar.addEventListener('click', function () {
      var consulta = (buscador.value || '').trim();
      if (!consulta) return;

      mensajeBuscador.textContent = 'Buscando...';

      var url = 'https://nominatim.openstreetmap.org/search?format=json&limit=1&countrycodes=pe&q=' +
        encodeURIComponent(consulta + ', Ica, Perú');

      fetch(url, { headers: { 'Accept': 'application/json' } })
        .then(function (res) { return res.json(); })
        .then(function (resultados) {
          if (!resultados || resultados.length === 0) {
            mensajeBuscador.textContent = 'No se encontró la dirección. Ajusta el marcador manualmente.';
            return;
          }
          var lat = parseFloat(resultados[0].lat);
          var lng = parseFloat(resultados[0].lon);
          mapa.setView([lat, lng], 16);
          marcador.setLatLng([lat, lng]);
          actualizarCoordenadas(lat, lng);
          mensajeBuscador.textContent = 'Ubicación encontrada. Ajusta el marcador si es necesario.';
        })
        .catch(function () {
          mensajeBuscador.textContent = 'No se pudo buscar la dirección. Intenta ubicarte manualmente en el mapa.';
        });
    });
  }
});