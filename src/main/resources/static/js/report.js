import { csrfHeader, csrfToken } from "./util/csrf.js";

class ReportPage {
    constructor() {
        this.testUuid = document.getElementById('testUuid').value;
        this.customerBikeId = document.getElementById('customerBikeId').value;
        this.initialBearingConditions = JSON.parse(document.getElementById('initialBearingConditionsJson').value || '[]');

        this.visualInspectionItems = JSON.parse(visualInspectionItems);
        this.functionalCheckItems = JSON.parse(functionalCheckItems);
        this.initialTestProcedures = JSON.parse(document.getElementById('testProceduresJson').value || '[]');

        this.form = document.getElementById('inspectionForm');

        this.initialTestProcScore = initialTestProcScore;
        this.initialNominalLoadContW = initialNominalLoadContW;
        this.initialNominalTempRiseCPerWh = initialNominalTempRiseCPerWh;
        this.initialNominalLoadScore = initialNominalLoadScore;
        this.initialBatteryTestCapacityWh = initialBatteryTestCapacityWh;
        this.initialBatteryTestHealthPct = initialBatteryTestHealthPct;
        this.initialBatteryTestScore = initialBatteryTestScore;
        this.initialBearingScore = initialBearingScore;

        this.initialize();
    }

    async initialize() {
        this.renderVisualInspection();
        this.renderFunctionalChecks();
        this.calculatedTestProcedures = this.initialTestProcedures;
        this.renderTestProcedures();
        this.populateDisplayScores();
        this.setupFormSubmit();
        this.renderBearingConditions();
    }

    renderBearingConditions() {
        const tableBody = document.querySelector('#bearingConditionsTable tbody');
        tableBody.innerHTML = '';

        const conditionsToRender = this.initialBearingConditions.length > 0 ? this.initialBearingConditions : [
            { name: "vooras / achteras", value: 0 },
            { name: "achteras", value: 0 },
            { name: "trapsas / motor", value: 0 }
        ];

        conditionsToRender.forEach(item => {
            const row = tableBody.insertRow();
            row.insertCell(0).textContent = item.name;

            const valueCell = row.insertCell(1);
            const input = document.createElement('input');
            input.type = 'text';
            input.className = 'form-control form-control-sm';
            input.name = `bearingConditions[${item.name}]`;
            input.value = item.value === 1 ? "good" : "bad";
            valueCell.appendChild(input);
        });
    }

    renderVisualInspection() {
        const table = document.getElementById('visualInspectionTable');
        table.innerHTML = `
            <thead><tr><th>Component</th><th>Conditie</th></tr></thead>
            <tbody>
                ${this.visualInspectionItems.map(item => `
                    <tr>
                        <td class="visual-label" data-english-label="${item.label}">${item.label}</td>
                        <td>
                            <select class="form-select form-select-sm visual-state-select" name="visualInspection[${item.label}]">
                                <option value="--" ${item.state === '--' ? 'selected' : ''}>--</option>
                                <option value="-" ${item.state === '-' ? 'selected' : ''}>-</option>
                                <option value=" " ${item.state === ' ' ? 'selected' : ''}> </option>
                                <option value="+" ${item.state === '+' ? 'selected' : ''}>+</option>
                                <option value="++" ${item.state === '++' ? 'selected' : ''}>++</option>
                                <option value="n.v.t" ${item.state === 'n.v.t' ? 'selected' : ''}>n.v.t</option>
                            </select>
                        </td>
                    </tr>
                `).join('')}
            </tbody>
        `;
    }

    renderFunctionalChecks() {
        const table = document.getElementById('functionalChecksTable');
        table.innerHTML = `
            <thead>
                <tr>
                    <th>Component</th>
                    <th>-</th>
                    <th>+</th>
                    <th>n.v.t</th>
                </tr>
            </thead>
            <tbody>
                ${this.functionalCheckItems.map(item => `
                    <tr>
                        <td class="functional-label" data-english-label="${item.label}">${item.label}</td>
                        <td><input type="radio" name="functionalChecks[${item.label}]" value="-" ${item.state === '-' ? 'checked' : ''}></td>
                        <td><input type="radio" name="functionalChecks[${item.label}]" value="+" ${item.state === '+' || item.state === undefined ? 'checked' : ''}></td>
                        <td><input type="radio" name="functionalChecks[${item.label}]" value="n.v.t" ${item.state === 'n.v.t' ? 'checked' : ''}></td>
                    </tr>
                `).join('')}
            </tbody>
        `;
    }

    renderTestProcedures() {
        const tableBody = document.querySelector('#testProceduresTable tbody');
        tableBody.innerHTML = '';

        if (!this.calculatedTestProcedures || this.calculatedTestProcedures.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="4">No test data available.</td></tr>';
            return;
        }

        this.calculatedTestProcedures.forEach(proc => {
            const row = tableBody.insertRow();
            row.insertCell(0).textContent = proc.name;
            row.insertCell(1).textContent = proc.result.toFixed(1);
            row.insertCell(2).textContent = proc.deviationPct.toFixed(1) + '%';
            row.insertCell(3).textContent = proc.unit;
        });
    }

    populateDisplayScores() {
        document.getElementById('displayTestProcScore').textContent = this.initialTestProcScore;
        document.getElementById('displayNominalLoadContW').textContent = this.initialNominalLoadContW;
        document.getElementById('displayNominalTempRiseCPerWh').textContent = this.initialNominalTempRiseCPerWh.toFixed(1);
        document.getElementById('displayNominalLoadScore').textContent = this.initialNominalLoadScore;
        document.getElementById('displayBatteryTestCapacityWh').textContent = this.initialBatteryTestCapacityWh;
        document.getElementById('displayBatteryTestHealthPct').textContent = this.initialBatteryTestHealthPct + '%';
        document.getElementById('displayBatteryTestScore').textContent = this.initialBatteryTestScore;
        document.getElementById('displayBearingScore').textContent = this.initialBearingScore;
    }

    setupFormSubmit() {
        this.form.addEventListener('submit', async (e) => {
            e.preventDefault();

            const formData = new FormData(this.form);

            const notes = {};
            this.form.querySelectorAll('input[type="checkbox"][name^="notes["]').forEach(checkbox => {
                if (checkbox.checked) {
                    const key = checkbox.name.match(/\[(.*?)\]/)[1];
                    notes[key] = checkbox.value;
                }
            });

            const visualInspection = this.collectVisualInspectionData();
            const functionalChecks = this.collectFunctionalChecksData();
            const bearingConditions = this.collectBearingConditionsData();

            const reportData = {
                testUuid: this.testUuid,
                customerBikeId: this.customerBikeId,
                visualInspection: visualInspection,
                functionalChecks: functionalChecks,
                comment: formData.get('comment'),
                notes: Object.values(notes),
                bearingConditions: bearingConditions,
                testProcedures: this.calculatedTestProcedures,

                brand: document.querySelector('input[name="brand"]').value,
                model: document.querySelector('input[name="model"]').value,
                firstRegistration: document.querySelector('input[name="firstRegistration"]').value,
                battery: document.getElementById('ebikeBattery').value,
                motorModel: document.querySelector('input[name="model"]').value,

                batteryCapacityWh: parseInt(document.querySelector('input[name="batteryCapacityWhInput"]').value),
                maxSupportPct: parseInt(document.querySelector('input[name="maxSupportPctInput"]').value),
                enginePowerMaxW: parseInt(document.querySelector('input[name="enginePowerMaxWInput"]').value),
                enginePowerNomW: parseInt(document.querySelector('input[name="enginePowerNomWInput"]').value),
                engineTorqueNm: parseInt(document.querySelector('input[name="engineTorqueNmInput"]').value),

                testProcScore: parseFloat(document.getElementById('testProcScore').value),
                nominalLoadContW: parseInt(document.getElementById('nominalLoadContW').value),
                nominalTempRiseCPerWh: parseFloat(document.getElementById('nominalTempRiseCPerWh').value),
                nominalLoadScore: parseFloat(document.getElementById('nominalLoadScore').value),
                batteryTestCapacityWh: parseInt(document.getElementById('batteryTestCapacityWh').value),
                batteryTestHealthPct: parseInt(document.getElementById('batteryTestHealthPct').value),
                batteryTestScore: parseFloat(document.getElementById('batteryTestScore').value),
                bearingScore: parseFloat(document.getElementById('displayBearingScore').textContent)
            };

            console.log("Submitting report data:", reportData);

            try {
                const response = await fetch('/api/reports', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        [csrfHeader]: csrfToken
                    },
                    body: JSON.stringify(reportData)
                });

                if (response.ok) {
                    window.location.href = '/technician/reports/success';
                } else {
                    const error = await response.json();
                    throw new Error(JSON.stringify(error));
                }
            } catch (error) {
                console.error('Error submitting report:', error);
                alert('Failed to submit report: ' + error.message);
            }
        });
    }

    collectVisualInspectionData() {
        const data = [];
        const table = document.getElementById('visualInspectionTable');
        table.querySelectorAll('tbody tr').forEach(row => {
            const labelCell = row.querySelector('.visual-label');
            const stateSelect = row.querySelector('.visual-state-select');
            if (labelCell && stateSelect) {
                data.push({
                    label: labelCell.textContent.trim(),
                    state: stateSelect.value
                });
            }
        });
        return data;
    }

    collectFunctionalChecksData() {
        const data = [];
        const table = document.getElementById('functionalChecksTable');
        table.querySelectorAll('tbody tr').forEach(row => {
            const labelCell = row.querySelector('.functional-label');
            const checkedRadio = row.querySelector('input[type="radio"]:checked');
            if (labelCell && checkedRadio) {
                data.push({
                    label: labelCell.textContent.trim(),
                    state: checkedRadio.value
                });
            }
        });
        return data;
    }

    collectBearingConditionsData() {
        const data = [];
        const table = document.getElementById('bearingConditionsTable');
        if (table) {
            const inputs = table.querySelectorAll('input[type="text"][name^="bearingConditions["]');
            inputs.forEach(input => {
                const nameMatch = input.name.match(/\[(.*?)\]/);
                if (nameMatch && nameMatch[1]) {
                    let valueToSave;
                    const inputValue = input.value.trim().toLowerCase();
                    if (inputValue === "good" || inputValue === "goed") {
                        valueToSave = 1;
                    } else if (inputValue === "bad" || inputValue === "slecht") {
                        valueToSave = 0;
                    } else {
                        valueToSave = parseInt(inputValue) || 0;
                    }
                    data.push({
                        name: nameMatch[1],
                        value: valueToSave
                    });
                }
            });
        }
        return data;
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new ReportPage();
});