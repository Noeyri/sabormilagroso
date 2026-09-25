/*
  Interruptor de modo claro/oscuro.
  Funciona con cualquier botón que tenga el atributo data-theme-toggle,
  así que sirve igual para el header público, el sidebar del admin y el del cliente.
  El dibujo (sol, luna, nubes, estrellas) vive en CSS y se activa solo con la clase
  "dark" en <html>; este script solo pone/quita esa clase y la recuerda.

  También escucha el evento "storage": si cambias el tema en una pestaña,
  todas las demás pestañas/ventanas abiertas del mismo sitio se actualizan solas.
*/
(function () {
    var CLAVE_ALMACENAMIENTO = 'sabormilagroso-theme';

    function actualizarBotones(esOscuro) {
        document.querySelectorAll('[data-theme-toggle]').forEach(function (boton) {
            boton.setAttribute('aria-pressed', String(esOscuro));
            boton.setAttribute('aria-label', esOscuro ? 'Cambiar a modo claro' : 'Cambiar a modo oscuro');
        });
    }

    function aplicarTema(esOscuro) {
        document.documentElement.classList.toggle('dark', esOscuro);
        actualizarBotones(esOscuro);
    }

    document.addEventListener('DOMContentLoaded', function () {
        actualizarBotones(document.documentElement.classList.contains('dark'));

        document.querySelectorAll('[data-theme-toggle]').forEach(function (boton) {
            boton.addEventListener('click', function () {
                var nuevoEstado = !document.documentElement.classList.contains('dark');
                aplicarTema(nuevoEstado);
                try {
                    localStorage.setItem(CLAVE_ALMACENAMIENTO, nuevoEstado ? 'dark' : 'light');
                } catch (e) {
                    /* almacenamiento no disponible, se ignora */
                }
            });
        });
    });

    // Sincroniza en tiempo real con otras pestañas/ventanas del mismo sitio
    window.addEventListener('storage', function (evento) {
        if (evento.key === CLAVE_ALMACENAMIENTO) {
            aplicarTema(evento.newValue === 'dark');
        }
    });
})();