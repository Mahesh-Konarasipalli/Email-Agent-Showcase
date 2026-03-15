<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AI Email Agent | Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <style>
        body {
            background-color: #f4f7f6;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .navbar-brand { font-weight: 700; letter-spacing: 0.5px; }
        .card { border-radius: 12px; border: none; }
        .table-custom th { background-color: #2c3e50; color: white; font-weight: 500; border-bottom: none; }
        .table-custom td { vertical-align: middle; padding: 1rem 0.75rem; }
        
        .analysis-text { 
            white-space: pre-wrap; 
            font-size: 0.85rem; 
            line-height: 1.5; 
            max-height: 120px; 
            overflow-y: auto; 
            display: block;
            background: #f8f9fa;
            padding: 10px;
            border-radius: 8px;
            border: 1px solid #e9ecef;
        }
        .analysis-text::-webkit-scrollbar { width: 6px; }
        .analysis-text::-webkit-scrollbar-thumb { background-color: #cbd5e1; border-radius: 10px; }
        
        .action-btns .btn { padding: 0.375rem 1rem; font-weight: 500; font-size: 0.85rem; }
        .empty-state { padding: 4rem 2rem; text-align: center; color: #6c757d; }
        
        /* Search bar styling */
        .search-container { position: relative; width: 100%; max-width: 300px; }
        .search-container .bi-search { position: absolute; left: 15px; top: 50%; transform: translateY(-50%); color: #6c757d; }
        .search-container input { padding-left: 40px; border-radius: 20px; }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4 shadow-sm">
    <div class="container">
        <a class="navbar-brand d-flex align-items-center gap-2" href="/dashboard">
            <i class="bi bi-robot text-info"></i> Mahesh's AI Agent
        </a>
    </div>
</nav>

<div class="container mb-5">
    
    <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center mb-4 gap-3">
        <div>
            <h2 class="fw-bold text-dark mb-1">Inbox Radar</h2>
            <p class="text-muted mb-0 small">Autonomous email analysis and drafting</p>
        </div>
        
        <div class="d-flex flex-column flex-sm-row gap-3 align-items-center">
            <div class="search-container shadow-sm">
                <i class="bi bi-search"></i>
                <input type="text" id="searchInput" class="form-control border-0" placeholder="Search sender or subject..." onkeyup="filterTable()">
            </div>
            
            <a href="/analyze-latest" class="btn btn-primary shadow-sm rounded-pill px-4 py-2 d-flex align-items-center gap-2">
                <i class="bi bi-arrow-clockwise"></i> Scan Inbox
            </a>
        </div>
    </div>

    <c:if test="${not empty message}">
        <div id="statusAlert" class="alert alert-${alertType} alert-dismissible fade show shadow-sm d-flex align-items-center" role="alert">
            <c:choose>
                <c:when test="${alertType == 'success'}"><i class="bi bi-check-circle-fill text-success me-2 fs-5"></i></c:when>
                <c:otherwise><i class="bi bi-exclamation-triangle-fill text-danger me-2 fs-5"></i></c:otherwise>
            </c:choose>
            <div><strong>Update:</strong> ${message}</div>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="card shadow">
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover table-custom mb-0" id="emailTable">
                    <thead>
                        <tr>
                            <th class="ps-4">Received</th>
                            <th>Sender</th>
                            <th style="width: 20%;">Subject</th>
                            <th style="width: 35%;">AI Analysis</th>
                            <th class="pe-4 text-center">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty summaries}">
                                <tr id="emptyRow">
                                    <td colspan="5">
                                        <div class="empty-state">
                                            <i class="bi bi-inbox fs-1 text-muted mb-3 d-block"></i>
                                            <h5>Your inbox is clear</h5>
                                            <p>No new emails have been analyzed recently.</p>
                                        </div>
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="email" items="${summaries}">
                                    <tr class="email-row">
                                        <td class="small text-muted ps-4">
                                            <i class="bi bi-calendar2-event me-1"></i>
                                            <span class="raw-date">${email.processedAt}</span>
                                        </td>
                                        <td class="sender-cell">
                                            <div class="fw-bold text-dark text-truncate" style="max-width: 150px;" title="${email.sender}">
                                                ${email.sender}
                                            </div>
                                        </td>
                                        <td class="subject-cell">
                                            <div class="small fw-semibold text-secondary text-truncate" style="max-width: 200px;" title="${email.subject}">
                                                ${email.subject}
                                            </div>
                                        </td>
                                        <td>
                                            <div class="analysis-text shadow-sm">${email.aiAnalysis}</div>
                                        </td>
                                        <td class="pe-4 align-middle">
                                            <div class="d-flex flex-column align-items-center gap-2 action-btns">
                                                
                                                <c:choose>
                                                    <c:when test="${email.replied}">
                                                        <span class="badge bg-success bg-opacity-10 text-success border border-success rounded-pill px-3 py-2 w-100">
                                                            <i class="bi bi-check2-all me-1"></i> Replied
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${fn:containsIgnoreCase(email.aiAnalysis, 'Marketing') or fn:containsIgnoreCase(email.aiAnalysis, 'Education') or fn:containsIgnoreCase(email.aiAnalysis, 'Spam')}">
                                                        <span class="badge bg-secondary bg-opacity-10 text-secondary border border-secondary rounded-pill px-3 py-2 w-100">
                                                            <i class="bi bi-slash-circle me-1"></i> No Reply
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <button type="button" class="btn btn-outline-primary rounded-pill w-100" data-bs-toggle="modal" data-bs-target="#draftModal${email.id}">
                                                            <i class="bi bi-pencil-square me-1"></i> Review
                                                        </button>

                                                        <div class="modal fade text-start" id="draftModal${email.id}" tabindex="-1" aria-hidden="true">
                                                            <div class="modal-dialog modal-lg modal-dialog-centered"> 
                                                                <div class="modal-content border-0 shadow-lg rounded-4">
                                                                    <form action="/send-reply/${email.id}" method="post">
                                                                        <div class="modal-header border-bottom-0 pt-4 px-4 bg-light rounded-top-4">
                                                                            <h5 class="modal-title fw-bold text-primary">
                                                                                <i class="bi bi-robot me-2"></i>AI Draft Review
                                                                            </h5>
                                                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                                        </div>
                                                                        <div class="modal-body px-4 py-4">
                                                                            <div class="card border border-light-subtle shadow-sm mb-4">
                                                                                <div class="card-body bg-light rounded">
                                                                                    <div class="d-flex mb-2">
                                                                                        <span class="text-muted small me-2" style="width: 60px;">To:</span>
                                                                                        <span class="fw-semibold small">${email.sender}</span>
                                                                                    </div>
                                                                                    <div class="d-flex">
                                                                                        <span class="text-muted small me-2" style="width: 60px;">Subject:</span>
                                                                                        <span class="fw-semibold small">Re: ${email.subject}</span>
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                            
                                                                            <label class="form-label small fw-bold text-secondary text-uppercase tracking-wider">Generated Response</label>
                                                                            <textarea class="form-control" name="editedDraft" rows="8" style="resize: vertical; font-size: 0.95rem;">${email.suggestedReply}</textarea>
                                                                            <div class="form-text mt-2"><i class="bi bi-info-circle me-1"></i>Feel free to edit this draft before sending.</div>
                                                                        </div>
                                                                        <div class="modal-footer border-top-0 pb-4 px-4">
                                                                            <button type="button" class="btn btn-light rounded-pill px-4" data-bs-dismiss="modal">Cancel</button>
                                                                            <button type="submit" class="btn btn-primary rounded-pill px-4 shadow-sm">
                                                                                <i class="bi bi-send-fill me-2"></i>Send Reply
                                                                            </button>
                                                                        </div>
                                                                    </form>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>

                                                <a href="/delete/${email.id}" class="btn btn-outline-danger rounded-pill w-100" onclick="return confirm('Are you sure you want to delete this summary?')">
                                                    <i class="bi bi-trash3 me-1"></i> Delete
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // --- Live Search Filter Logic ---
    function filterTable() {
        const input = document.getElementById("searchInput").value.toLowerCase();
        const rows = document.querySelectorAll(".email-row");
        
        rows.forEach(row => {
            const sender = row.querySelector(".sender-cell").innerText.toLowerCase();
            const subject = row.querySelector(".subject-cell").innerText.toLowerCase();
            
            // Check if the typed text exists in the sender OR subject columns
            if (sender.includes(input) || subject.includes(input)) {
                row.style.display = ""; // Show row
            } else {
                row.style.display = "none"; // Hide row
            }
        });
    }

    document.addEventListener("DOMContentLoaded", function() {
        // Auto-dismiss alerts smoothly
        var alertElement = document.getElementById('statusAlert');
        if (alertElement) {
            setTimeout(function() {
                var bsAlert = new bootstrap.Alert(alertElement);
                bsAlert.close();
            }, 4000);
        }

        // Clean up raw Java LocalDateTime strings
        document.querySelectorAll('.raw-date').forEach(function(el) {
            var rawText = el.innerText.trim();
            if (rawText) {
                var date = new Date(rawText);
                if (!isNaN(date)) {
                    el.innerText = date.toLocaleString('en-US', { 
                        month: 'short', day: 'numeric', 
                        hour: 'numeric', minute: '2-digit', hour12: true 
                    });
                }
            }
        });
    });
</script>
</body>
</html>