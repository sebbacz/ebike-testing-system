let daUuid;
class ReportDisplayPage {
    constructor() {
        this.reportUuid = this.getReportUuidFromUrl();
        if (this.reportUuid) {
            this.fetchAndDisplayReport(this.reportUuid)
            this.reportUuid = daUuid;
        } else {
            console.error("Report UUID not found in URL.");
            document.querySelector('.container-fluid').innerHTML = '<div class="alert alert-danger" role="alert">Report not found. Please provide a valid UUID.</div>';
        }
    }

    getReportUuidFromUrl() {
        const pathParts = window.location.pathname.split('/');
        const uuid = pathParts[pathParts.length - 1];
        if (uuid && uuid.match(/^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i)) {
            return uuid;
        }
        return null;
    }

    async fetchAndDisplayReport(uuid) {
        try {
            const response = await fetch(`/api/reports/${uuid}`);
            if (!response.ok) {
                if (response.status === 404) {
                    throw new Error("Report not found for the given UUID.");
                } else {
                    throw new Error(`Failed to fetch report: ${response.status} ${response.statusText}`);
                }
            }
            const reportDto = await response.json();
            console.log("Fetched Report DTO:", reportDto);
            this.populatePage(reportDto);
        } catch (error) {
            console.error("Error fetching report:", error);
            document.querySelector('.container-fluid').innerHTML = `<div class="alert alert-danger" role="alert">${error.message || 'An error occurred while loading the report.'}</div>`;
        }
    }

    populatePage(reportDto) {
        // Dates
        const createdOn = reportDto.createdOn;
        const createdDate = new Date(createdOn);
        if (!isNaN(createdDate)) {
            document.getElementById('reportDate').textContent = this.formatDateTime(reportDto.createdOn);
        } else {
            document.getElementById('reportDate').textContent = 'Invalid date';
        }

        // UUID
        document.getElementById('testUuidDisplay').value = reportDto.testUuid || 'N/A';

        // Overall Score
        document.getElementById('overallScoreDisplay').textContent = reportDto.overallScore !== undefined && reportDto.overallScore !== null
            ? reportDto.overallScore.toFixed(0) : 'N/A';

        // Identification
        document.getElementById('brandDisplay').value = reportDto.brand || 'N/A';
        document.getElementById('modelDisplay').value = reportDto.model || 'N/A';
        document.getElementById('driveTypeDisplay').value = reportDto.motorModel || 'N/A';

        const fRegDate = new Date(reportDto.firstRegistration);
        if (!isNaN(fRegDate)) {
            document.getElementById('firstRegistrationDisplay').value = this.formatDateTime(reportDto.firstRegistration);
        } else {
            document.getElementById('firstRegistrationDisplay').textContent = 'Invalid date';
        }


        document.getElementById('batteryCapacityWhDisplay').value = reportDto.batteryCapacityWh !== undefined && reportDto.batteryCapacityWh !== null
            ? reportDto.batteryCapacityWh.toFixed(0) : 'N/A';
        document.getElementById('maxSupportPctDisplay').value = reportDto.maxSupportPct !== undefined && reportDto.maxSupportPct !== null
            ? reportDto.maxSupportPct.toFixed(0) : 'N/A';
        document.getElementById('enginePowerMaxWDisplay').value = reportDto.enginePowerMaxW !== undefined && reportDto.enginePowerMaxW !== null
            ? reportDto.enginePowerMaxW.toFixed(0) : 'N/A';
        document.getElementById('enginePowerNomWDisplay').value = reportDto.enginePowerNomW !== undefined && reportDto.enginePowerNomW !== null
            ? reportDto.enginePowerNomW.toFixed(0) : 'N/A';
        document.getElementById('engineTorqueNmDisplay').value = reportDto.engineTorqueNm !== undefined && reportDto.engineTorqueNm !== null
            ? reportDto.engineTorqueNm.toFixed(0) : 'N/A';

        // Visual Inspection
        this.renderVisualInspectionTable(reportDto.visualInspection);

        // Comments
        document.getElementById('commentDisplay').value = reportDto.comment || 'No comments.';

        // Test Procedures
        this.renderTestProceduresTable(reportDto.testProcedures);
        document.getElementById('displayTestProcScore').textContent = reportDto.testProcScore !== undefined && reportDto.testProcScore !== null
            ? reportDto.testProcScore.toFixed(0) : 'N/A';

        // Nominal Load
        document.getElementById('displayNominalLoadContW').textContent = reportDto.nominalLoadContW !== undefined && reportDto.nominalLoadContW !== null
            ? reportDto.nominalLoadContW.toFixed(0) : 'N/A';
        document.getElementById('displayNominalTempRiseCPerWh').textContent = reportDto.nominalTempRiseCPerWh !== undefined && reportDto.nominalTempRiseCPerWh !== null
            ? reportDto.nominalTempRiseCPerWh.toFixed(1) : 'N/A';
        document.getElementById('displayNominalLoadScore').textContent = reportDto.nominalLoadScore !== undefined && reportDto.nominalLoadScore !== null
            ? reportDto.nominalLoadScore.toFixed(0) : 'N/A';

        // Battery Test
        document.getElementById('displayBatteryTestCapacityWh').textContent = reportDto.batteryTestCapacityWh !== undefined && reportDto.batteryTestCapacityWh !== null
            ? reportDto.batteryTestCapacityWh.toFixed(0) : 'N/A';
        document.getElementById('displayBatteryTestHealthPct').textContent = reportDto.batteryTestHealthPct !== undefined && reportDto.batteryTestHealthPct !== null
            ? reportDto.batteryTestHealthPct.toFixed(0) + '%' : 'N/A';
        document.getElementById('displayBatteryTestScore').textContent = reportDto.batteryTestScore !== undefined && reportDto.batteryTestScore !== null
            ? reportDto.batteryTestScore.toFixed(0) : 'N/A';

        // Bearing Health
        this.renderBearingConditionsTable(reportDto.bearingConditions);
        document.getElementById('displayBearingScore').textContent = reportDto.bearingScore !== undefined && reportDto.bearingScore !== null
            ? reportDto.bearingScore.toFixed(0) : 'N/A';

        // Functional Operation
        this.renderFunctionalChecksTable(reportDto.functionalChecks);
    }

    renderVisualInspectionTable(items) {
        const tableBody = document.querySelector('#visualInspectionTable tbody');
        tableBody.innerHTML = '';
        if (items && items.length > 0) {
            items.forEach(item => {
                const row = tableBody.insertRow();
                row.insertCell(0).textContent = item.label || 'N/A';
                row.insertCell(1).textContent = item.state || 'N/A';
            });
        } else {
            tableBody.innerHTML = '<tr><td colspan="2">No visual inspection data.</td></tr>';
        }
    }

    renderTestProceduresTable(procedures) {
        const tableBody = document.querySelector('#testProceduresTable tbody');
        tableBody.innerHTML = '';
        if (procedures && procedures.length > 0) {
            procedures.forEach(proc => {
                const row = tableBody.insertRow();
                row.insertCell(0).textContent = proc.name || 'N/A';
                row.insertCell(1).textContent = proc.result !== undefined && proc.result !== null ? proc.result.toFixed(1) : 'N/A';
                row.insertCell(2).textContent = proc.deviationPct !== undefined && proc.deviationPct !== null ? proc.deviationPct.toFixed(1) + '%' : 'N/A';
                row.insertCell(3).textContent = proc.unit || 'N/A';
            });
        } else {
            tableBody.innerHTML = '<tr><td colspan="4">No test procedure data.</td></tr>';
        }
    }

    renderBearingConditionsTable(conditions) {
        const tableBody = document.querySelector('#bearingConditionsTable tbody');
        tableBody.innerHTML = '';
        if (conditions && conditions.length > 0) {
            conditions.forEach(condition => {
                const row = tableBody.insertRow();
                row.insertCell(0).textContent = condition.name || 'N/A';
                let displayValue = 'N/A';
                if (condition.value === 1) {
                    displayValue = "Good";
                } else if (condition.value === 0) {
                    displayValue = "Bad";
                } else {
                    displayValue = String(condition.value);
                }
                row.insertCell(1).textContent = displayValue;
            });
        } else {
            tableBody.innerHTML = '<tr><td colspan="2">No bearing conditions data.</td></tr>';
        }
    }

    renderFunctionalChecksTable(items) {
        const tableBody = document.querySelector('#functionalChecksTable tbody');
        tableBody.innerHTML = '';
        if (items && items.length > 0) {
            items.forEach(item => {
                const row = tableBody.insertRow();
                row.insertCell(0).textContent = item.label || 'N/A';
                row.insertCell(1).textContent = item.state || 'N/A';
            });
        } else {
            tableBody.innerHTML = '<tr><td colspan="2">No functional checks data.</td></tr>';
        }
    }

    formatDateTime(dateString) {
        const date = new Date(dateString);
        if (isNaN(date)) return 'Invalid date';

        const pad = (num) => String(num).padStart(2, '0');

        const hours = pad(date.getHours());
        const minutes = pad(date.getMinutes());
        const day = pad(date.getDate());
        const month = pad(date.getMonth() + 1); // Month is 0-indexed
        const year = date.getFullYear();

        return `${hours}:${minutes} - ${day}/${month}/${year}`;
    }



}

document.addEventListener('DOMContentLoaded', () => {
    new ReportDisplayPage();
});

document.addEventListener("DOMContentLoaded", () => {
    const downloadBtn = document.getElementById('downloadPdfBtn');
    if (!downloadBtn) return;

    downloadBtn.addEventListener('click', () => {
        const reportElement = document.getElementById('pdf-section');

        const options = {
            margin: 0.2, // balanced
            filename: `report_${daUuid}.pdf`,
            image: { type: 'jpeg', quality: 0.98 },
            html2canvas: {
                scale: 2.5, // smoother text
                useCORS: true,
                allowTaint: true,
                scrollX: 0,
                scrollY: 0,
                windowWidth: document.body.scrollWidth, // captures full layout width
                windowHeight: document.body.scrollHeight
            },
            jsPDF: {
                unit: 'in',
                format: 'a4',
                orientation: 'landscape'
            },
            pagebreak: {
                mode: ['css', 'legacy'] // drop 'avoid-all' – it interferes with Bootstrap rows
            }
        };

        html2pdf().set(options).from(reportElement).save();
    });
});



