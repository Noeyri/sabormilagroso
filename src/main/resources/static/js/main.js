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

  // Menú móvil del área Cliente (Fase 2)
  var clienteToggle = document.getElementById('cliente-menu-toggle');
  var clienteMenu = document.getElementById('cliente-mobile-menu');
  if (clienteToggle && clienteMenu) {
    clienteToggle.addEventListener('click', function () {
      clienteMenu.classList.toggle('hidden');
      var icon = clienteToggle.querySelector('.material-symbols-outlined');
      if (icon) {
        icon.textContent = clienteMenu.classList.contains('hidden') ? 'menu' : 'close';
      }
    });
  }

  // Menú móvil del área Admin
  var adminToggle = document.getElementById('admin-menu-toggle');
  var adminMenu = document.getElementById('admin-mobile-menu');
  if (adminToggle && adminMenu) {
    adminToggle.addEventListener('click', function () {
      adminMenu.classList.toggle('hidden');
      var icon = adminToggle.querySelector('.material-symbols-outlined');
      if (icon) {
        icon.textContent = adminMenu.classList.contains('hidden') ? 'menu' : 'close';
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

  // --- MOTOR INTERACTIVO DE FILTRADO, BÚSQUEDA Y ORDENAMIENTO (Menú público) ---
  var searchInput = document.getElementById('searchInput');
  var sortSelect = document.getElementById('sortSelect');
  var categoryButtons = document.querySelectorAll('.btn-categoria');
  var menuItems = document.querySelectorAll('.producto-item');
  var menuGrid = document.getElementById('menuGrid');
  var noResultsMessage = document.getElementById('noResultsMessage');

  if (menuItems.length > 0 && menuGrid) {
    var currentCategory = 'Todos';
    var currentSearchTerm = '';
    var currentSort = 'relevancia';

    var applyFilters = function () {
      var itemsArray = Array.from(menuItems);

      // 1. Ordenamiento
      itemsArray.sort(function (a, b) {
        switch (currentSort) {
          case 'precio_asc':
            return parseFloat(a.dataset.precio) - parseFloat(b.dataset.precio);
          case 'precio_desc':
            return parseFloat(b.dataset.precio) - parseFloat(a.dataset.precio);
          case 'popular':
            return (b.dataset.popular === 'true' ? 1 : 0) - (a.dataset.popular === 'true' ? 1 : 0);
          default:
            return 0; // Relevancia (orden original)
        }
      });

      // Reordenar elementos en el DOM
      itemsArray.forEach(function (item) { menuGrid.appendChild(item); });

      // 2. Filtrado por categoría y búsqueda de texto
      var visibleCount = 0;
      itemsArray.forEach(function (item) {
        var itemCat = item.dataset.categoria;
        var itemName = item.dataset.nombre.toLowerCase();

        var matchCategory = (currentCategory === 'Todos' || itemCat === currentCategory);
        var matchSearch = itemName.includes(currentSearchTerm);

        if (matchCategory && matchSearch) {
          item.style.display = 'flex';
          visibleCount++;
        } else {
          item.style.display = 'none';
        }
      });

      // 3. Controlar mensaje si no hay resultados
      if (noResultsMessage) {
        if (visibleCount === 0) {
          noResultsMessage.classList.remove('hidden');
          menuGrid.classList.add('hidden');
        } else {
          noResultsMessage.classList.add('hidden');
          menuGrid.classList.remove('hidden');
        }
      }
    };

    // Evento: Escribir en el buscador
    if (searchInput) {
      searchInput.addEventListener('input', function (e) {
        currentSearchTerm = e.target.value.toLowerCase().trim();
        applyFilters();
      });
    }

    // Evento: Cambiar opción de ordenamiento
    if (sortSelect) {
      sortSelect.addEventListener('change', function (e) {
        currentSort = e.target.value;
        applyFilters();
      });
    }

    // Evento: Clic en botones de categorías
    categoryButtons.forEach(function (btn) {
      btn.addEventListener('click', function () {
        categoryButtons.forEach(function (b) {
          b.className = 'btn-categoria px-4 py-2 rounded-full bg-surface-container-lowest border border-outline-variant text-on-surface-variant hover:bg-secondary-container hover:text-on-secondary-container hover:border-transparent font-label-md text-label-md transition-all';
        });

        btn.className = 'btn-categoria px-4 py-2 rounded-full bg-primary-container text-on-primary-container font-label-md text-label-md shadow-sm transition-all';

        currentCategory = btn.getAttribute('data-categoria');
        applyFilters();
      });
    });
  }
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