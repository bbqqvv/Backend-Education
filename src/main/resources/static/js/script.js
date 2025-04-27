$(document).ready(function() {
    // Xử lý form cài đặt thời gian
    $('#timeSettingsForm').on('submit', function(e) {
        e.preventDefault();

        $.ajax({
            url: '/update_settings',
            method: 'POST',
            data: $(this).serialize(),
            success: function(response) {
                if (response.success) {
                    showAlert('success', 'Cập nhật thành công!');
                } else {
                    showAlert('danger', 'Lỗi: ' + (response.error || 'Không rõ nguyên nhân'));
                }
            },
            error: function() {
                showAlert('danger', 'Lỗi kết nối server');
            }
        });
    });

    // Hiển thị thông báo
    function showAlert(type, message) {
        const alertHtml = `
            <div class="alert alert-${type} alert-dismissible fade show" role="alert">
                ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        `;
        $('.card-body').prepend(alertHtml);

        // Tự động đóng thông báo sau 5s
        setTimeout(() => {
            $('.alert').alert('close');
        }, 5000);
    }

    // Cập nhật thống kê
    function updateStats() {
        $.get('/api/stats', function(data) {
            $('.display-4').eq(0).text(data.present || 0);
            $('.display-4').eq(1).text(data.late || 0);
            $('.display-4').eq(2).text(data.absent || 0);
        });
    }

    // Cập nhật mỗi phút
    updateStats();
    setInterval(updateStats, 60000);
});