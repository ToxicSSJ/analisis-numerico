document.addEventListener("DOMContentLoaded", function() {

    if (!window.location.pathname.startsWith('/cholesky')) {
        return;
    }

    console.log("Cholesky Decomposition page loaded");

    const form = document.getElementById('cholesky-form');
    const vectorInput = document.getElementById('vector-input');
    const resultText = document.getElementById('result-text');
    const resultSolution = document.getElementById('result-solution');
    const resultMatrixL = document.getElementById('result-matrix-l');
    const resultMatrixU = document.getElementById('result-matrix-u');

    var hot = new Handsontable(document.getElementById('matrix-input'), {
        data: Handsontable.helper.createSpreadsheetData(3, 3),
        rowHeaders: true,
        colHeaders: true,
        contextMenu: true,
        licenseKey: 'non-commercial-and-evaluation' // Ensure to get the correct license for your use case
    });

    form.addEventListener('submit', function(event) {
        event.preventDefault();

        const matrixData = hot.getData();
        const formattedMatrix = matrixData.map(row => row.join(' ')).join('; ');
        const vectorData = vectorInput.value.trim();

        const params = new URLSearchParams({
            matrix: formattedMatrix,
            vector: vectorData
        });

        // Enviar los datos al backend como parámetros de consulta
        fetch(`${currentUrl()}/api/v1/cholesky?${params.toString()}`, {
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
        resultText.innerHTML = '<p>Solution and Matrices:</p><p>Message: ' + result.message + '</p>';
        console.log("Message = " + result["message"])

        resultSolution.innerHTML = "";
        resultMatrixL.innerHTML = "";
        resultMatrixU.innerHTML = "";

        // Mostrar solución
        const solutionData = result.solution.map(val => [val]);
        new Handsontable(resultSolution, {
            data: solutionData,
            readOnly: true,
            rowHeaders: false,
            colHeaders: ['Solution'],
            licenseKey: 'non-commercial-and-evaluation'
        });

        // Mostrar matriz L
        new Handsontable(resultMatrixL, {
            data: result.l,
            readOnly: true,
            rowHeaders: true,
            colHeaders: true,
            licenseKey: 'non-commercial-and-evaluation'
        });

        // Mostrar matriz U
        new Handsontable(resultMatrixU, {
            data: result.u,
            readOnly: true,
            rowHeaders: true,
            colHeaders: true,
            licenseKey: 'non-commercial-and-evaluation'
        });
    }
});
