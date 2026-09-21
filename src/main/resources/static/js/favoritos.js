// Sabor Milagroso - Favoritos: alterna el corazón sin recargar la página.
// Si fetch falla (sesión vencida, red, etc.), se envía el formulario normalmente.
document.addEventListener('submit', function (e) {
  var form = e.target;
  if (!form || !form.classList || !form.classList.contains('form-favorito')) return;

  e.preventDefault();

  var boton = form.querySelector('.btn-favorito');
  var icono = form.querySelector('.material-symbols-outlined');
  if (boton) boton.disabled = true;

  fetch(form.action, {
    method: 'POST',
    credentials: 'same-origin',
    headers: {
      'X-Requested-With': 'fetch',
      'Accept': 'application/json'
    },
    body: new URLSearchParams(new FormData(form))
  })
    .then(function (res) {
      if (!res.ok) throw new Error('Error ' + res.status);
      return res.json();
    })
    .then(function (data) {
      if (icono) {
        icono.style.fontVariationSettings = data.favorito ? "'FILL' 1" : "'FILL' 0";
      }
      if (boton) {
        boton.setAttribute('aria-label', data.favorito ? 'Quitar de favoritos' : 'Agregar a favoritos');
      }
      if (typeof showToast === 'function') {
        showToast(data.favorito ? 'Agregado a favoritos' : 'Quitado de favoritos');
      }
      if (boton) boton.disabled = false;
    })
    .catch(function () {
      form.submit();
    });
});