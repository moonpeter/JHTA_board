function getList(state) {
    option = state; // 현재 선택한 댓글 정렬 방식을 저장합니다. 1=>등록순, 2=>최신순

    $.ajax({
        type: "post",
        url: "CommentList.bo",
        data: {"page": 1, "comment_board_num": $("#board_re_ref").val(), state: state},
        dataType: "json",
        success: function (rdata) {
            $('#count').text(rdata.listcount).css('font-family', 'arial, sans-serif')
            var red1 = 'red';
            var red2 = 'red';
            if (option == 1) {
                red2 = 'gray';
            }
            if (option == 2) {
                red1 = 'gray';
            }
            var output = "";

            if (rdata.boardlist.length > 0) {
                output += '<li class="comment_tab_item ' + red1 + '" >'
                    + '<a href="javascript:getList(1)" class="comment_tab_button">등록순</a>'
                    + '</li>'
                    + '<li class="comment_tab_item ' + red2 + '" >'
                    + '<a href="javascript:getList(2)" class="comment_tab_button">최신순</a>'
                    + '</li>';
                $('.comment_tab_list').html(output);
                output = '';
                $(rdata.boardlist).each(function () {
                    var lev = this.comment_re_lev;
                    var comment_reply = '';
                    if (lev == 1) {
                        comment_reply = ' CommentItem--reply lev1';
                    } else if (lev == 2) {
                        comment_reply = ' CommentItem--reply lev2';
                    }
                    var profile = this.memberfile;
                    var src = '/image/profile.png';
                    if (profile) {
                        src = 'memberupload/' + profile;
                    }

                    output += '<li id="' + this.num + '" class="CommentItem' + comment_reply + '">'
                        + '<div class="comment_area">'
                        + '<img src="' + src + '" alt="프로필 사진" width="36" height="36">'
                        + '<div class="comment_box">'
                        + '<div class="comment_nick_box">'
                        + '<div class="comment_nick_info">'
                        + '<div class="comment_nickname">' + this.id + '</div>'
                        + '</div>'
                        + '</div>'
                        + '</div>'
                        + '<div class="commnet_text_box">'
                        + '<p class="comment_text_view">'
                        + '<span class="text_comment">' + this.content + '</span>'
                        + '</p>'
                        + '</div>'
                        + '<div class="comment_info_box">'
                        + '<span class="comment_info_date">' + this.reg_date + '</span>';
                    if (lev < 2) {
                        output += '<a href="javascript:replyform(' + this.num + ',' + lev + ',' + this.comment_re_seq + ','
                            + this.comment_re_ref + ')" class="comment_info_button">답글쓰기</a>'
                    }
                    output += "</div>"

                    if ($("#loginid").val() == this.id) {
                        output += '<div class="comment_tool">'
                            + '<div title="더보기" class="comment_tool_button">'
                            + '<div>&#46;&#46;&#46;</div>'
                            + '</div>'
                            + '<div id="commentItem' + this.num + '" class="LayerMore">'
                            + '<ul class="layer_list">'
                            + '<li class="layer_item">'
                            + '<a href="javascript:updateForm(' + this.num + ')"'
                            + 'class="layer_button">수정</a>&nbsp;&nbsp;'
                            + '<a href="javascript:del(' + this.num + ')"'
                            + 'class="layer_button">삭제</a></li></ul>'
                            + '</div>'
                            + '</div>'
                    }

                    output += '</div>';
                    output += '</div>'
                        + '</li>'
                })
                $('.comment_list').html(output);
            }
        }
    });
}

$(function (){
    option = 1;
    getList(option); // 처음 로드 될때는 등록순 정

    $("form").submit(function (){
        if($("#board_pass").val()=='') {
            alert("비밀번호를 입력하세요");
            $("#board_pass").focus();
            return false;
        }
    })

    $('.CommentBox').on('keyup', '.comment_inbox_text', function (){
        var length=$(this).val().length;
        $(this).prev().text(length+'/200');
    });

    //댓글 등록을 클릭하면 데이터베이스에 저장 -> 저장 성공 후에 리스트 불러옵니다.
    $('ul+.CommentWriter .btn_register').click(function (){
        var content=$('.comment_inbox_text').val();
        if(!content){ //내용없이 등록 클릭한 경우
            alert("댓글을 입력하세요");
            return;
        }

        $.ajax({
            url : 'CommentAdd.bo', //원문 등록
            data : {
                id : $("#loginid").val(),
                content : content,
                comment_board_num: $("#board_re_ref").val(),
                comment_re_lev : 0,
                comment_re_seq: 0
            },
            type : 'post',
            success : function (rdata) {
                if(rdata ==1){
                    getList(option);
                }
            }
        })

        $('.comment_inbox_text').val(''); // textarea 초기화
    })

})