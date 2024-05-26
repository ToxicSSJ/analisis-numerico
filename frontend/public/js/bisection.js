import { javaUrl, rustUrl } from '/config.js'

document.addEventListener("DOMContentLoaded", function() {
    
    console.log(javaUrl)

    if (!window.location.pathname.startsWith('/bisection')) {
        return;
    }

    console.log("Bisection page loaded");

    const form = document.getElementById('bisection-form');
    const functionInput = document.getElementById('function-input');
    const latexOutput = document.getElementById('latex-output');

    var functionValue;

    form.addEventListener('submit', function(event) {
        event.preventDefault();
        
        const formData = new FormData(form);
        functionValue = formData.get('function');
        const params = new URLSearchParams({
            function: formData.get('function'),
            a: formData.get('a'),
            b: formData.get('b'),
            precisionType: formData.get('precisionType'),
            errorType: formData.get('errorType'),
            toleranceValue: formData.get('toleranceValue'),
            maxIterations: formData.get('maxIterations')
        });

        fetch(`${javaUrl}/api/v1/bisection?${params.toString()}`, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(result => {
            const prettyResult = JSON.stringify(result, null, 2);
            console.log('Formatted Result:', prettyResult);
            displayResult(result);
        })
        .catch(error => {
            console.error('Error:', error);
        });
    });

    function displayResult(result) {
        document.getElementById('result-text').textContent = result.message;
        
        const resultTable = document.getElementById('result-table');
        resultTable.innerHTML = '';

        if (result.iterations && result.xvals && result.fvals && result.errors) {
            result.iterations.forEach((iteration, index) => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${iteration}</td>
                    <td>${result.xvals[index]}</td>
                    <td>${result.fvals[index]}</td>
                    <td>${result.errors[index]}</td>
                `;
                resultTable.appendChild(row);
            });

            const s = result.xvals[result.xvals.length - 1];
            console.log("S=" + s)
            plotFunction(functionValue, s);
        } else {
            console.error('Invalid result data', result);
            alert('Invalid result data');
        }
    }

    functionInput.addEventListener('input', function() {
        const latexString = toLaTeX(functionInput.value);
        latexOutput.textContent = `$$${latexString}$$`;
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, latexOutput]);
    });

    function plotFunction(mathFunction, s) {
        const xvals = [];
        const yvals = [];
        const scope = {};

        console.log("Mathfunction="+mathFunction)
        const node = math.parse(mathFunction);

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