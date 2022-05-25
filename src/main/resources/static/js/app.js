const main = {
    init: function () {
        const _this = this;

        // 글 삭제
        $('#btn-delete').on('click', function () {
            _this.delete();
        });

        $('#btn-comment-save').on('click', function () {
            _this.commentSave();
        });
    },

    /** 글 삭제 */
    delete: function () {
        const id = $('#id').val();
        const con_check = confirm("정말 삭제하시겠습니까?");

        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        if (con_check === true) {
            $.ajax({
                type: 'DELETE',
                contentType: 'application/json; charset=utf-8',
                url: '/api/post/' + id,
                beforeSend: function (xhr) {
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

    /** 댓글 저장 */
    commentSave: function () {
        const postId = $('#id').val();
        const data = {
            content: $('#content').val()
        }

        if (data.content.trim() === "") {
            alert("댓글 내용을 입력하세요.")
        } else {
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
            $.ajax({
                type: 'POST',
                contentType: 'application/json; charset=utf-8',
                url: '/api/posts/' + postId + '/comments',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                dataType: 'JSON',
                data: JSON.stringify(data)
            }).done(function () {
                alert("댓글이 등록되었습니다.");
                window.location.href='/posts/' + postId;
            }).fail(function () {
                alert("댓글 등록에 실패했습니다.");
            });
        }
    },
};

main.init();