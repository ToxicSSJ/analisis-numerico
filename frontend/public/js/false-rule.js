import { javaUrl, rustUrl } from '/config.js'

document.addEventListener("DOMContentLoaded", function() {

    if (!window.location.pathname.startsWith('/false-rule')) {
        return;
    }

    console.log("False Position page loaded");

    const form = document.getElementById('false-position-form');
    const functionInput = document.getElementById('function-input');
    const latexOutput = document.getElementById('latex-output');

    var functionValue;

    form.addEventListener('submit', function(event) {
        event.preventDefault();
        
        const formData = new FormData(form);
        functionValue = formData.get('function');
        const params = new URLSearchParams({
            function: formData.get('function'),
            lowerBound: formData.get('lowerBound'),
            upperBound: formData.get('upperBound'),
            errorType: formData.get('errorType'),
            precisionType: formData.get('precisionType'),
            toleranceValue: formData.get('toleranceValue'),
            maxIterations: formData.get('maxIterations')
        });

        // Enviar los datos al backend como parámetros de consulta
        fetch(`${apiBaseUrl}/api/v1/false-rule?${params.toString()}`, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(result => {
            // Convertir resultado en JSON y aplicar prettifier
            const prettyResult = JSON.stringify(result, null, 2);
            console.log('Formatted Result:', prettyResult);
            displayResult(result);
        })
        .catch(error => {
            console.error('Error:', error);
        });
    });

    // Función para mostrar los resultados
    function displayResult(result) {
        document.getElementById('result-text').textContent = result.message;
        
        const resultTable = document.getElementById('result-table');
        resultTable.innerHTML = ''; // Limpiar tabla

        if (result.iterations && result.xvalues && result.functionValues && result.errors) {
            result.iterations.forEach((iteration, index) => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${iteration}</td>
                    <td>${result.xvalues[index]}</td>
                    <td>${result.functionValues[index]}</td>
                    <td>${result.errors[index]}</td>
                `;
                resultTable.appendChild(row);
            });

            const s = result.xvalues[result.xvalues.length - 1];
            console.log("S=" + s)
            plotFunction(functionValue, s);
        } else {
            console.error('Invalid result data', result);
            alert('Invalid result data');
        }
    }

    // Actualizar la representación LaTeX en tiempo real
    functionInput.addEventListener('input', function() {
        const latexString = toLaTeX(functionInput.value);
        latexOutput.textContent = `$$${latexString}$$`;
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, latexOutput]);
    });

    function plotFunction(mathFunction, s) {
        const xvals = [];
        const yvals = [];
        const scope = {};

        // Convertir la función matemática a un nodo que se puede evaluar con math.js
        console.log("Mathfunction="+mathFunction)
        const node = math.parse(mathFunction);

        // Rango de valores para x, desde s-20 hasta s+20
        const startX = s - 20;
        const endX = s + 20;
        const step = 0.1;

        for (let x = startX; x <= endX; x += step) {
            scope.x = x;
            try {
                const y = node.evaluate(scope);
                xvals.push(x);
                yvals.push(y);
            } catch (err) {
                console.error(`Error evaluating function at x=${x}:`, err);
            }
        }

        const trace = {
            x: xvals,
            y: yvals,
            type: 'scatter'
        };

        const layout = {
            title: 'Function Graphic',
            xaxis: {
                title: 'x'
            },
            yaxis: {
                title: 'f(x)'
            }
        };

        Plotly.newPlot('result-chart', [trace], layout);
    }

    // Función para convertir a LaTeX usando mathjs
    function toLaTeX(mathExpr) {
        try {
            const node = math.parse(mathExpr);
            return node.toTex();
        } catch (err) {
            console.error('Error parsing math expression:', err);
            return mathExpr;
        }
    }
});
