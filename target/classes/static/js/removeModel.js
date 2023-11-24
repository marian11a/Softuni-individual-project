function removeModel(modelId) {
    var confirmDelete = window.confirm("Are you sure you want to delete this model?");

    if (confirmDelete) {
        $.ajax({
            type: "POST",
            url: "/api/remove-model",
            data: { modelId: modelId },
            success: function (response) {
                $('#model_' + modelId).fadeOut(500, function () {
                    $(this).remove();
                });
            },
            error: function (error) {
                alert("Error removing model");
            }
        });
    }

    return false;
}
