require('dotenv').config();

const express = require('express');
const path = require('path');
const app = express();

app.set('view engine', 'pug');
app.set('views', path.join(__dirname, 'views'));

app.use(express.static(path.join(__dirname, 'public')));

app.get('/', (req, res) => {
  res.render('index');
});

app.get('/config.js', (req, res) => {
  res.type('application/javascript');
  res.send(`const apiBaseUrl = '${process.env.API_BASE_URL}'; export { apiBaseUrl };`);
});

app.get('/bisection', (req, res) => {
  res.render('bisection');
});

app.get('/false-rule', (req, res) => {
  res.render('false-rule');
});

app.get('/fixed-point', (req, res) => {
  res.render('fixed-point');
});

app.get('/incremental-search', (req, res) => {
  res.render('incremental-search');
});

app.get('/multiple-roots', (req, res) => {
  res.render('multiple-roots');
});

app.get('/newton-raphson', (req, res) => {
  res.render('newton-raphson');
});

app.get('/secant', (req, res) => {
  res.render('secant');
});

app.get('/cholesky', (req, res) => {
  res.render('cholesky');
});

app.get('/crout', (req, res) => {
  res.render('crout');
});

app.get('/doolittle', (req, res) => {
  res.render('doolittle');
});

app.get('/gaussian-elimination', (req, res) => {
  res.render('gaussian-elimination');
});

app.get('/lu-decomposition', (req, res) => {
  res.render('lu-decomposition');
});

app.get('/jacobi', (req, res) => {
  res.render('jacobi');
});

app.get('/gauss-seidel', (req, res) => {
  res.render('gauss-seidel');
});

const PORT = process.env.PORT || 4790;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});