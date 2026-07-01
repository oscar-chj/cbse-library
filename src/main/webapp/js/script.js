let activeHiddenTrigger = null;

function showDeleteConfirm(buttonEl) {
  const modal = document.getElementById("deleteConfirmModal");
  const titleSpan = document.getElementById("deleteConfirmBookTitle");
  const card = buttonEl.closest(".store-utility-card");
  if (card) {
    if (titleSpan) {
      const titleEl = card.querySelector(".book-title-text");
      titleSpan.textContent = titleEl ? titleEl.textContent : "this book";
    }
    activeHiddenTrigger = card.querySelector(
      '[id$="hiddenDeleteTrigger"]',
    );
  }
  if (modal) {
    modal.classList.add("show");
  }
}

function closeDeleteConfirm() {
  const modal = document.getElementById("deleteConfirmModal");
  if (modal) {
    modal.classList.remove("show");
  }
  activeHiddenTrigger = null;
}

function proceedDelete() {
  if (activeHiddenTrigger) {
    activeHiddenTrigger.click();
  }
  closeDeleteConfirm();
}

function dismissToast(closeBtn) {
  const toast = closeBtn.closest(".toast-notification");
  if (toast) {
    toast.style.opacity = "0";
    setTimeout(() => {
      if (toast.parentNode) {
        toast.parentNode.removeChild(toast);
      }
    }, 300);
  }
}

function initToasts() {
  const notifications = document.querySelectorAll(
    ".toast-notification:not(.initialized)",
  );
  notifications.forEach((el) => {
    el.classList.add("initialized");
    // Auto-dismiss after 4 seconds
    setTimeout(() => {
      el.style.opacity = "0";
      setTimeout(() => {
        if (el.parentNode) {
          el.parentNode.removeChild(el);
        }
      }, 300);
    }, 4000);
  });
}

function registerAjaxListener() {
  if (window.jsf) {
    jsf.ajax.addOnEvent(function (data) {
      if (data.status === "success") {
        initToasts();
      }
    });
  }
}

document.addEventListener("DOMContentLoaded", () => {
  initToasts();
  registerAjaxListener();

  // Dismiss toast click delegation
  document.addEventListener("click", (event) => {
    const closeBtn = event.target.closest(".ui-growl-icon-close");
    if (closeBtn) {
      dismissToast(closeBtn);
    }
  });

  // Delete card button delegation
  document.addEventListener("click", (event) => {
    const deleteBtn = event.target.closest(".delete-card-btn");
    if (deleteBtn) {
      showDeleteConfirm(deleteBtn);
    }
  });

  // Modal Cancel Button
  document.addEventListener("click", (event) => {
    if (event.target.id === "btn-cancel-delete" || event.target.closest("#btn-cancel-delete")) {
      closeDeleteConfirm();
    }
  });

  // Modal Confirm Button
  document.addEventListener("click", (event) => {
    if (event.target.id === "btn-confirm-delete" || event.target.closest("#btn-confirm-delete")) {
      proceedDelete();
    }
  });
});

