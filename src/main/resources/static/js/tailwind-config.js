/*
  Configuración de Tailwind CSS (vía CDN) para Sabor Milagroso.
  Basada en el Design System de Stitch: sabor_peruano_design_system/DESIGN.md
  Paleta: naranja cálido + crema + blanco + marrón oscuro.
 */
tailwind.config = {
  darkMode: "class",
  theme: {
    extend: {
      colors: {
        "on-primary-fixed": "#370e00",
        "white": "#FFFFFF",
        "on-surface": "#231a14",
        "on-primary-fixed-variant": "#7f2b00",
        "tertiary-fixed-dim": "#ffb780",
        "on-tertiary": "#ffffff",
        "surface-container-lowest": "#ffffff",
        "error-red": "#C94C4C",
        "bg-cream-light": "#FFF9F3",
        "tertiary-container": "#a96428",
        "on-tertiary-container": "#fffbff",
        "on-secondary-fixed-variant": "#56423b",
        "primary-fixed": "#ffdbce",
        "primary-fixed-dim": "#ffb599",
        "tertiary-fixed": "#ffdcc4",
        "surface-tint": "#a63b01",
        "surface-container-low": "#fff1ea",
        "surface-dim": "#e8d7ce",
        "hover-orange": "#C94F20",
        "surface-container-highest": "#f1dfd6",
        "on-primary": "#ffffff",
        "bg-cream-primary": "#FFF4E6",
        "on-secondary-fixed": "#271811",
        "success-green": "#5C8D61",
        "background": "#fff8f5",
        "on-secondary-container": "#745e55",
        "surface-container": "#fdeae1",
        "primary-container": "#c4501a",
        "on-error": "#ffffff",
        "secondary": "#705a51",
        "surface-variant": "#f1dfd6",
        "primary": "#a23900",
        "on-secondary": "#ffffff",
        "outline": "#8b7168",
        "surface": "#fff8f5",
        "inverse-surface": "#392e28",
        "surface-container-high": "#f7e5dc",
        "inverse-on-surface": "#ffede5",
        "error-container": "#ffdad6",
        "inverse-primary": "#ffb599",
        "tertiary": "#8b4c11",
        "on-primary-container": "#fffbff",
        "secondary-container": "#f8d9cf",
        "outline-variant": "#dfc0b5",
        "secondary-fixed": "#fadcd1",
        "secondary-fixed-dim": "#ddc0b6",
        "on-error-container": "#93000a",
        "on-tertiary-fixed-variant": "#6f3800",
        "on-background": "#231a14",
        "on-surface-variant": "#58423a",
        "surface-bright": "#fff8f5",
        "on-tertiary-fixed": "#2f1400",
        "error": "#ba1a1a"
      },
      borderRadius: {
        DEFAULT: "0.25rem",
        lg: "0.5rem",
        xl: "0.75rem",
        full: "9999px"
      },
      spacing: {
        "container-max": "1200px",
        "margin-desktop": "64px",
        "margin-mobile": "20px",
        base: "8px",
        gutter: "24px"
      },
      fontFamily: {
        "headline-md": ["EB Garamond", "serif"],
        "headline-lg-mobile": ["EB Garamond", "serif"],
        "headline-xl": ["EB Garamond", "serif"],
        "headline-sm": ["EB Garamond", "serif"],
        "label-md": ["Hanken Grotesk", "sans-serif"],
        "body-lg": ["Hanken Grotesk", "sans-serif"],
        "body-md": ["Hanken Grotesk", "sans-serif"],
        "headline-lg": ["EB Garamond", "serif"],
        "label-sm": ["Hanken Grotesk", "sans-serif"]
      },
      fontSize: {
        "headline-md": ["28px", { lineHeight: "36px", fontWeight: "500" }],
        "headline-lg-mobile": ["32px", { lineHeight: "40px", fontWeight: "600" }],
        "headline-xl": ["48px", { lineHeight: "56px", letterSpacing: "-0.02em", fontWeight: "600" }],
        "headline-sm": ["22px", { lineHeight: "28px", fontWeight: "500" }],
        "label-md": ["14px", { lineHeight: "20px", letterSpacing: "0.01em", fontWeight: "600" }],
        "body-lg": ["18px", { lineHeight: "28px", fontWeight: "400" }],
        "body-md": ["16px", { lineHeight: "24px", fontWeight: "400" }],
        "headline-lg": ["36px", { lineHeight: "44px", fontWeight: "600" }],
        "label-sm": ["12px", { lineHeight: "16px", fontWeight: "500" }]
      }
    }
  }
};