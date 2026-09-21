// Sabor Milagroso - Mapa interactivo para seleccionar la ubicación del cliente
// Leaflet + OpenStreetMap. Sin Google Maps, sin API key, sin facturación.
// Búsqueda de direcciones mediante Nominatim (servicio gratuito de OpenStreetMap).
// El mismo modal sirve para crear y para editar una dirección.
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
  var form = document.getElementById('form-direccion');
  var tituloModal = document.getElementById('titulo-modal-direccion');
  var btnGuardar = document.getElementById('btn-guardar-direccion');

  if (!modal || !btnAbrir || typeof L === 'undefined') return;

  // Centro inicial: Ica, Perú
  var CENTRO_ICA = { lat: -14.0678, lng: -75.7286 };
  var MENSAJE_INICIAL = 'Busca tu dirección o ajusta el marcador manualmente en el mapa.';

  // Acción original del formulario (crear). Al editar se reemplaza temporalmente.
  var accionNueva = form.getAttribute('action');

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
  }

  // Muestra el modal con el marcador en la posición indicada
  function mostrarModal(lat, lng, zoom) {
    modal.classList.remove('hidden');
    inicializarMapa();
    marcador.setLatLng([lat, lng]);
    actualizarCoordenadas(lat, lng);
    mapa.setView([lat, lng], zoom);
    // El mapa se inicializa mientras el modal estaba oculto (0px de alto),
    // por lo que hay que recalcular su tamaño al mostrarlo.
    setTimeout(function () {
      mapa.invalidateSize();
      mapa.setView([lat, lng], zoom);
    }, 50);
  }

  function abrirModalNuevo() {
    form.reset();
    form.setAttribute('action', accionNueva);
    tituloModal.textContent = 'Nueva dirección';
    btnGuardar.textContent = 'Guardar dirección';
    mensajeBuscador.textContent = MENSAJE_INICIAL;
    mostrarModal(CENTRO_ICA.lat, CENTRO_ICA.lng, 14);
  }

  function abrirModalEdicion(boton) {
    form.reset();
    form.setAttribute('action', boton.dataset.action);
    tituloModal.textContent = 'Editar dirección';
    btnGuardar.textContent = 'Guardar cambios';
    mensajeBuscador.textContent = MENSAJE_INICIAL;

    document.getElementById('etiqueta').value = boton.dataset.etiqueta || '';
    document.getElementById('direccion').value = boton.dataset.direccion || '';
    document.getElementById('distrito').value = boton.dataset.distrito || '';
    document.getElementById('referencia').value = boton.dataset.referencia || '';

    var lat = parseFloat(boton.dataset.latitud);
    var lng = parseFloat(boton.dataset.longitud);
    var tieneCoordenadas = !isNaN(lat) && !isNaN(lng);
    mostrarModal(
      tieneCoordenadas ? lat : CENTRO_ICA.lat,
      tieneCoordenadas ? lng : CENTRO_ICA.lng,
      tieneCoordenadas ? 16 : 14
    );
  }

  function cerrarModal() {
    modal.classList.add('hidden');
  }

  btnAbrir.addEventListener('click', abrirModalNuevo);
  if (btnCerrar) btnCerrar.addEventListener('click', cerrarModal);
  modal.addEventListener('click', function (e) {
    if (e.target === modal) cerrarModal();
  });

  document.querySelectorAll('.btn-editar-direccion').forEach(function (boton) {
    boton.addEventListener('click', function () {
      abrirModalEdicion(boton);
    });
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