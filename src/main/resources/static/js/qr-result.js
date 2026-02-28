import {csrfHeader, csrfToken} from "./util/csrf.js";

const customerId = document.body.getAttribute("data-customer-id");
const bikeId = document.body.getAttribute("data-ebike-id");


let currentTestId = null;


function formatDateTime(dateTimeString) {
    if (!dateTimeString) return '-';
    const date = new Date(dateTimeString);
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear().toString().slice(-2);
    return `${hours}:${minutes} ${day}/${month}/${year}`;
}


// function attachResultsRedirectHandlers() {
//     document.querySelectorAll(".redirect-test-results").forEach(button => {
//         button.addEventListener("click", () => {
//             const uuid   = button.dataset.id;
//             if (!uuid) {
//                 console.error("Missing UUID or bikeId for redirection.");
//                 return;
//             }
//             if (!customerId) {
//                 console.error("Missing customerId");
//                 return;
//             }
//             if (!bikeId) {
//                 console.error("Missing bikeId." + bikeId + customerId);
//                 return;
//             }
//
//             // New URL:
//             window.location.href =
//                 `/technician/customers/${customerId}/bikes/${bikeId}/test-results/${uuid}`;
//         });
//     });
// }

function attachResultsRedirectHandlers() {
    document.querySelectorAll(".redirect-test-results").forEach(button => {
        button.addEventListener("click", () => {
            const uuid = button.dataset.id;
            if (!uuid) {
                console.error("Missing UUID for redirection.");
                return;
            }
            window.location.href = `/test-results/${uuid}`;
        });
    });
}

function attachReportRedirectHandlers() {
    document.querySelectorAll(".redirect-test-report").forEach(button => {
        button.addEventListener("click", () => {
            const uuid = button.dataset.id;
            if (!uuid) {
                console.error("Missing UUID for redirection.");
                return;
            }
            window.location.href = `/reports/${uuid}`;
        });
    });
}


async function loadTests() {
    const tbody = document.getElementById("test-table-body");
    tbody.innerHTML = "<tr><td colspan='13'>Loading...</td></tr>";

    if (!bikeId) {
        console.error("Bike ID is missing.");
        tbody.innerHTML = '<tr><td colspan="12">Error: Bike ID is missing.</td></tr>';
        return;
    }

    const url = `/api/tests/bike/${bikeId}`;

    try {
        const response = await fetch(url);
        if (!response.ok) throw new Error("Failed to fetch tests.");
        const tests = await response.json();

        if (tests.length === 0) {
            tbody.innerHTML = '<tr><td colspan="12">No tests found.</td></tr>';
        } else {
            tbody.innerHTML = tests.map(test => `
                <tr>
                  <td>${test.id}</td>
                  <td>${test.ebikeId}</td>
                  <td>${test.status ?? '-'}</td>
                  <td>${formatDateTime(test.startTime)}</td>
                  <td>${formatDateTime(test.endTime)}</td>
                  <td>${test.type ?? '-'}</td>
                  ${test.status === "COMPLETED" ? `<td><button type="button" class="btn btn-sm btn-info active redirect-test-results" data-id="${test.uuid}">Results</button></td>` :
                `<td><button type="button" class="btn btn-sm btn-info disabled redirect-test-results" data-id="${test.uuid}">Results</button></td>`}
                     <td class="report-button-cell" data-uuid="${test.uuid}" data-status="${test.status}">
                    <button type="button" class="btn btn-sm btn-warning disabled" data-id="${test.uuid}">Report</button></td>
                  <td><button type="button" class="btn btn-sm btn-primary refresh-test-table" data-id="${test.id}">Refresh</button></td>
                  <td><button type="button" class="btn btn-sm btn-secondary details-btn" data-id="${test.id}">Details</button></td>
                </tr>
            `).join("");

            attachRefreshHandlers();
            attachDetailsHandlers();
            attachResultsRedirectHandlers();
            attachReportRedirectHandlers();
            await updateReportButtons();

        }
    } catch (err) {
        console.error("Error loading tests:", err);
        tbody.innerHTML = '<tr><td colspan="12">Error loading tests.</td></tr>';
    }
}

function attachRefreshHandlers() {
    document.querySelectorAll('.refresh-test-table').forEach(button => {
        button.addEventListener('click', async () => {
            const testId = button.dataset.id;
            const row = button.closest('tr');

            try {
                const response = await fetch(`/api/tests/${testId}`);
                if (!response.ok) throw new Error("Failed to fetch test data.");

                const test = await response.json();

                row.innerHTML = `
                    <td>${test.id}</td>
                    <td>${test.ebikeId}</td>
                    <td>${test.status ?? '-'}</td>
                    <td>${formatDateTime(test.startTime)}</td>
                    <td>${formatDateTime(test.endTime)}</td>
                    <td>${test.type ?? '-'}</td>
                    ${test.status === "COMPLETED" ? `<td><button type="button" class="btn btn-sm btn-info redirect-test-results active" data-id="${test.uuid}">Results</button></td>` :
                    `<td><button type="button" class="btn btn-sm btn-info disabled redirect-test-results disabled" data-id="${test.uuid}">Results</button></td>`}
                     <td class="report-button-cell" data-uuid="${test.uuid}" data-status="${test.status}">
                    <button type="button" class="btn btn-sm btn-warning disabled" data-id="${test.uuid}">Report</button></td>
                    <td><button type="button" class="btn btn-sm btn-primary refresh-test-table" data-id="${test.id}">Refresh</button></td>
                    <td><button type="button" class="btn btn-sm btn-secondary details-btn" data-id="${test.id}" aria-pressed="true">Details</button></td>
                `;

                attachRefreshHandlers();
                // attachDeleteHandlers();
                attachDetailsHandlers();
                attachResultsRedirectHandlers();
                attachReportRedirectHandlers();

                const reportCell = row.querySelector(".report-button-cell");
                const uuid = reportCell.getAttribute("data-uuid");
                const status = reportCell.getAttribute("data-status");

                // Fetch report status for this test
                try {
                    const res = await fetch(`/api/reports/${uuid}`);
                    if (res.ok && status === "COMPLETED") {
                        reportCell.innerHTML = `<button type="button" class="btn btn-sm btn-warning active redirect-test-report" data-id="${uuid}">Report</button>`;
                    } else {
                        reportCell.innerHTML = `<button type="button" class="btn btn-sm btn-warning disabled redirect-test-report" data-id="${uuid}">Report</button>`;
                    }

                } catch (err) {
                    console.error("Failed to check report for uuid:", uuid, err);
                }

                attachReportRedirectHandlers(); // reattach again if updated
            } catch (err) {
                console.error("Error refreshing test row:", err);
                alert("Could not refresh test data.");
            }
        });
    });
}

function attachDetailsHandlers() {
    document.querySelectorAll('.details-btn').forEach(button => {
        button.addEventListener('click', async () => {
            const testId = button.dataset.id;

            try {
                const response = await fetch(`/api/tests/${testId}`);
                if (!response.ok) throw new Error("Failed to fetch test details.");

                const test = await response.json();

                const fields = {
                    "ID": test.id,
                    "eBike ID": test.ebikeId,
                    "Technician ID": test.technicianId ?? "-",
                    "Customer ID": test.customerId ?? "-",
                    "Start Time": formatDateTime(test.startTime),
                    "End Time": formatDateTime(test.endTime),
                    "Status": test.status ?? "-",
                    "Type": test.type ?? "-",
                    "batteryCapacity": test.batteryCapacity ?? "-",
                    "maxSupport": test.maxSupport ?? "-",
                    "enginePowerMax": test.enginePowerMax ?? "-",
                    "enginePowerNominal": test.enginePowerNominal ?? "-",
                    "engineTorque": test.engineTorque ?? "-",
                    "Report": test.report ?? "-",
                    "UUID": test.uuid ?? "-"
                };

                const tbody = document.getElementById("testDetailsBody");
                const entries = Object.entries(fields);

                let html = "";
                // Iterate through all but the last field in pairs
                for (let i = 0; i < entries.length - 1; i += 2) {
                    const [key1, val1] = entries[i];
                    const [key2, val2] = entries[i + 1];

                    html += `
                        <tr>
                            <th class="w-25">${key1}</th>
                            <td class="w-25">${val1}</td>
                            <th class="w-25">${key2}</th>
                            <td class="w-25">${val2}</td>
                        </tr>
                    `;
                }

                // Add the last field alone, spanning full row (4 columns)
                const [lastKey, lastVal] = entries[entries.length - 1];
                html += `
                    <tr>
                        <th colspan="1">${lastKey}</th>
                        <td colspan="3">${lastVal}</td>
                    </tr>
                `;

                tbody.innerHTML = html;


                currentTestId = test.id;

                const modalEl = document.getElementById('testDetailsModal');
                const modalInstance = bootstrap.Modal.getOrCreateInstance(modalEl);
                modalInstance.show();

            } catch (err) {
                console.error("Error showing test details:", err);
                alert("Could not load test details.");
            }
        });
    });
}

async function updateReportButtons() {
    const cells = document.querySelectorAll(".report-button-cell");

    for (const cell of cells) {
        const uuid = cell.getAttribute("data-uuid");
        const status = cell.getAttribute("data-status");


        try {
            const response = await fetch(`/api/reports/${uuid}`);
            if (response.ok) {
                cell.innerHTML = `<button type="button" class="btn btn-sm btn-warning active redirect-test-report" data-id="${uuid}">Report</button>`;
            } else {
                cell.innerHTML = `<button type="button" class="btn btn-sm btn-warning disabled redirect-test-report" data-id="${uuid}">Report</button>`;
            }
        } catch (err) {
            console.error("Failed to check report for uuid:", uuid, err);
            cell.innerHTML = `<button type="button" class="btn btn-sm btn-warning disabled redirect-test-report" data-id="${uuid}">Report</button>`;
        }
    }

    attachResultsRedirectHandlers(); // re-attach since buttons were re-rendered
    attachReportRedirectHandlers(); // re-attach since buttons were re-rendered
}


document.addEventListener("DOMContentLoaded", () => {
    loadTests().then(() => {
        // attachDeleteHandlers();
        attachRefreshHandlers();
        attachDetailsHandlers();
        attachResultsRedirectHandlers();
        attachReportRedirectHandlers();
    });
});
