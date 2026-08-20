// Sabor Milagroso - Comportamiento común del área pública (Fase 1 - solo frontend)
document.addEventListener('DOMContentLoaded', function () {
  var toggle = document.getElementById('mobile-menu-toggle');
  var menu = document.getElementById('mobile-menu');

  if (toggle && menu) {
    toggle.addEventListener('click', function () {
      menu.classList.toggle('open');
      var icon = toggle.querySelector('.material-symbols-outlined');
      if (icon) {
        icon.textContent = menu.classList.contains('open') ? 'close' : 'menu';
      }
    });
  }

  // Interceptar formularios de esta fase (sin backend real todavía) y
  // mostrar una confirmación visual con datos mock.
  document.querySelectorAll('form[data-mock-submit]').forEach(function (form) {
    form.addEventListener('submit', function (e) {
      e.preventDefault();
      var msg = form.getAttribute('data-mock-submit') || 'Acción registrada (modo demostración).';
      showToast(msg);
    });
  });
});

function showToast(message) {
  var existing = document.querySelector('.toast');
  if (existing) existing.remove();

  var toast = document.createElement('div');
  toast.className = 'toast bg-inverse-surface text-inverse-on-surface font-label-md text-label-md px-6 py-4 rounded-lg shadow-md';
  toast.textContent = message;
  document.body.appendChild(toast);

  setTimeout(function () {
    toast.style.opacity = '0';
    setTimeout(function () { toast.remove(); }, 300);
  }, 2500);
}