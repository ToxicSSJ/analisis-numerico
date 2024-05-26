import { javaUrl, rustUrl } from '/config.js'

document.addEventListener("DOMContentLoaded", function() {

    if (!window.location.pathname.startsWith('/gauss-seidel')) {
        return;
    }

    console.log("Gauss-Seidel Iterative Method page loaded");

    const form = document.getElementById('gauss-seidel-form');
    const vectorInput = document.getElementById('vector-input');
    const initialGuessInput = document.getElementById('initial-guess');
    const resultText = document.getElementById('result-text');
    const resultTable = document.getElementById('result-table');
    const resultSolution = document.getElementById('result-solution');

    var hot = new Handsontable(document.getElementById('matrix-input'), {
        data: Handsontable.helper.createSpreadsheetData(3, 3),
        rowHeaders: true,
        colHeaders: true,
        contextMenu: true,
        licenseKey: 'non-commercial-and-evaluation'
    });

    form.addEventListener('submit', function(event) {
        event.preventDefault();

        const matrixData = hot.getData();
        const formattedMatrix = matrixData.map(row => row.join(' ')).join('; ');
        const vectorData = vectorInput.value.trim();
        const initialGuessData = initialGuessInput.value.trim();
        const formData = new FormData(form);

        const params = new URLSearchParams({
            size: matrixData.length,
            matrixData: formattedMatrix,
            b: vectorData,
            x0: initialGuessData,
            errorType: formData.get('errorType'),
            toleranceValue: formData.get('toleranceValue'),
            maxIterations: formData.get('maxIterations'),
            norm: formData.get('norm')
        });

        fetch(`${apiBaseUrl}/api/v1/gauss-seidel?${params.toString()}`, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(result => {
            console.log('Formatted Result:', result);
            displayResult(result);
        })
        .catch(error => {
            console.error('Error:', error);
        });
    });

    function displayResult(result) {
        resultText.innerHTML = result.message;

        resultTable.innerHTML = "";
        resultSolution.innerHTML = "";

        result.errors.forEach((error, index) => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${index + 1}</td>
                <td>${error}</td>
            `;
            resultTable.appendChild(row);
        });

        new Handsontable(resultSolution, {
            data: result.xvalues,
            readOnly: true,
            rowHeaders: true,
            colHeaders: true,
            licenseKey: 'non-commercial-and-evaluation',
            colWidths: 100,
            rowHeights: 30
        });
    }
});