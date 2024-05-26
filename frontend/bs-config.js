module.exports = {
    proxy: "localhost:3000",
    files: ["views/**/*.pug", "public/**/*.*"],
    port: 3001,
    open: false,
    notify: false
};