import { javaUrl, otherUrl } from '/config.js'

window.currentUrl = function() {
    const selectedLanguage = localStorage.getItem('programmingLanguage');
    if(selectedLanguage) {
        if(selectedLanguage == "Python") {
            return otherUrl;
        }
    }
    return javaUrl;
}

window.selectLanguage = function(language, iconSrc) {
    localStorage.setItem('programmingLanguage', language);
    localStorage.setItem('programmingLanguageIcon', iconSrc);
    updateDropdownTitle(language, iconSrc);
    
    // Cerrar el dropdown automáticamente después de seleccionar un lenguaje
    const dropdown = document.querySelector('.navbar-item.has-dropdown.is-active');
    if (dropdown) {
        dropdown.classList.remove('is-active');
    }

    bulmaToast.toast({
        message: `Selected ` + language + ` succesfully!`,
        type: 'is-info',
        position: 'bottom-right',
        duration: 3000,
        opacity: 0.8
    })
};

window.toggleDropdown = function(event) {
    event.preventDefault();
    const dropdown = event.currentTarget.parentNode;
    dropdown.classList.toggle('is-active');
};

// Función para actualizar el título del dropdown
function updateDropdownTitle(language, iconSrc) {
    const selectedLanguageElement = document.getElementById('selected-language');
    const selectedLanguageIconElement = document.getElementById('selected-language-icon');
    selectedLanguageElement.innerHTML = `<strong>Backend:</strong> ${language}`;
    selectedLanguageIconElement.src = iconSrc;
    selectedLanguageIconElement.alt = language;
}

// Hacer las funciones globales para que puedan ser llamadas desde el HTML
window.updateDropdownTitle = updateDropdownTitle;

// Comprobación fuera del DOMContentLoaded para verificar ejecución del script
console.log("Script is loaded");

// Función que se ejecuta cuando el DOM está completamente cargado
function onDomContentLoaded() {
    console.log("DOM fully loaded and parsed");

    const selectedLanguage = localStorage.getItem('programmingLanguage');
    const selectedLanguageIcon = localStorage.getItem('programmingLanguageIcon');
    if (selectedLanguage && selectedLanguageIcon) {
        updateDropdownTitle(selectedLanguage, selectedLanguageIcon);
    }
}

// Verificación si el documento ya está completamente cargado
if (document.readyState === 'complete' || document.readyState === 'interactive') {
    // Documento ya cargado, ejecutar inmediatamente
    onDomContentLoaded();
} else {
    // Añadir event listener para DOMContentLoaded
    document.addEventListener("DOMContentLoaded", onDomContentLoaded);
}