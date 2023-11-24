function makeAdmin(userId) {
    var confirmDelete = window.confirm("Are you sure you want to make this user ADMIN?");

    if (confirmDelete) {
        $.ajax({
            type: "POST",
            url: "/api/make-admin",
            data: { userId: userId },
            success: function (response) {
                location.reload();
            },
            error: function (error) {
                alert("Error making ADMIN");
            }
        });
    }

    return false;
}

function removeAdmin(userId) {
    var confirmDelete = window.confirm("Are you sure you want to remove admin role from this user?");

    if (confirmDelete) {
        $.ajax({
            type: "POST",
            url: "/api/remove-admin",
            data: { userId: userId },
            success: function (response) {
                location.reload();
            },
            error: function (error) {
                alert("Error removing admin role");
            }
        });
    }

    return false;
}
