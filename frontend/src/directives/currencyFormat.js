// src/directives/currencyFormat.js
export default {
    bind(el) {
        const formatValue = (value) => {
            let numberValue = parseFloat(value.replace(/[^0-9.-]/g, ''));
            if (isNaN(numberValue)) {
                numberValue = 0;
            }
            return numberValue.toLocaleString('en-US', {
                style: 'currency',
                currency: 'USD',
                minimumFractionDigits: 2,
                maximumFractionDigits: 2,
            });
        };

        const parseValue = (value) => {
            return value.replace(/[^0-9.-]/g, '');
        };

        el.addEventListener('input', function (e) {
            const parsedValue = parseValue(e.target.value);
            const formattedValue = formatValue(parsedValue);
            e.target.value = parsedValue; // Update the model value
            e.target.dataset.rawValue = parsedValue; // Store the raw value
            const event = new Event('input', { bubbles: true });
            e.target.dispatchEvent(event);
        });

        el.addEventListener('blur', function (e) {
            e.target.value = formatValue(e.target.dataset.rawValue || e.target.value);
        });

        el.addEventListener('focus', function (e) {
            e.target.value = e.target.dataset.rawValue || parseValue(e.target.value);
        });

        // Initial formatting
        el.value = formatValue(el.value);
    },
};
