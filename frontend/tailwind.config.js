module.exports = {
    content: ['./public/**/*.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
    theme: {
        extend: {
            fontFamily: {
                knewave: ['"Knewave"', 'cursive'], // Knewave with cursive as a fallback
                marker: ['"Permanent Marker"', 'cursive'],
            },
        },
    },
    plugins: [],
};
