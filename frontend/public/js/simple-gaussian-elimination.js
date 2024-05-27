document.addEventListener("DOMContentLoaded", function() {

    if (!window.location.pathname.startsWith('/simple-gaussian-elimination')) {
        return;
    }

    console.log("Simple Gaussian Elimination page loaded");

    const form = document.getElementById('gaussian-elimination-form');
    const vectorInput = document.getElementById('vector-input');
    const resultText = document.getElementById('result-text');
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

        const params = new URLSearchParams({
            matrixA: formattedMatrix,
            vectorB: vectorData
        });

        fetch(`${currentUrl()}/api/v1/simple-gaussian-elimination?${params.toString()}`, {
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

        resultSolution.innerHTML = "";

        const solutionData = result.solution.map(val => [val]);
        new Handsontable(resultSolution, {
            data: solutionData,
            readOnly: true,
            rowHeaders: false,
            colHeaders: ['Solution'],
            licenseKey: 'non-commercial-and-evaluation'
        });
    }
});
