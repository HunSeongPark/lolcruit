const main = {
    init : function() {
        const _this = this;

        // 글 삭제
        $('#btn-delete').on('click', function () {
            _this.delete();
        });
    },

    /** 글 삭제 */
    delete : function () {
        const id = $('#id').val();
        const con_check = confirm("정말 삭제하시겠습니까?");
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        if(con_check === true) {
            $.ajax({
                type: 'DELETE',
                contentType: 'application/json; charset=utf-8',
                url: '/api/post/'+id,
                beforeSend : function(xhr) {
                    xhr.setRequestHeader(header, token);
                },
                dataType: 'JSON',

            }).done(function () {
                alert("삭제되었습니다.");
                window.location.href = '/';
            }).fail(function () {
                alert("게시글 삭제에 실패했습니다.");
            });
        } else {
            return false;
        }
    },
};

main.init();