function go(page) {
    var limit = $('#viewcount').val();
    var data = "limit=" + limit + "&state=ajax&page=" + page;
    ajax(data);
}

function setPaging(href, digit) {
    output += "<li class=page-item>";
    gray="";
    if(href=="") {
        gray = " gray";
    }
    anchor = "<a class='page-link" + gray + "'" + href + ">" + digit + "</a></li>";
    output += anchor;
}

function ajax(sdata) {
    console.log(sdata)
    output="";
    $.ajax({
        type : "POST",
        data : sdata,
        url : "BoardList.bo",
        dataType : "json",
        cache : false,
        success : function (data) {
            $("#viewcount").val(data.limit);
            $("table").find("font").text("글 개수 : " + data.listcount);

            if (data.listcount >0) { // 총갯수가 0보다 큰 경우
                $("tbody").remove();
                var num = data.listcount = (data.page -1) * data.limit;
                console.log(num)
                output = "<tbody>";
                $(data.boardlist).each(
                    function (index, item) {
                        output += '<tr><td>' + (num--) + '</td>'
                        blank_count = item.board_re_lev * 2+ 1;
                        blank = '&nbsp;';
                        for (var i=0; i<blank_count; i++) {
                            blank += '&nbsp;&nbsp;';
                        }
                        img="";
                        if(item.board_re_lev>0){
                            img="<img src='/image/line.gif'>";
                        }

                        output += "<td><div>" + blank + img
                        output += ' <a href ="BoardDetailAction.bo?num=' + item.board_num + '">'
                        output += item.board_subject.replace(/</g,'&lt;').replace(/>/g,'&gt;') + '</a></div></td>'
                        output += '<td><div>' + item.board_name + '</div></td>'
                        output += '<td><div>' + item.board_date + '</div></td>'
                        output += '<td><div>' + item.board_re_readcount + '</div></td></tr>'
                    })
                output += "</tbody>"
                $('table').append(output) // table 완성

                $(".pagination").empty(); // 페이징 처리 영역 내용 제거
                output ="";

                digit = '이전&nbsp;'
                href="";
                if(data.page >1) {
                    href = 'href=javascript:go(' + (data.page -1) + ')';
                }
                setPaging(href, digit);

                for(var i = data.startpage; i<=data.endpage; i++){
                    digit = i;
                    href = "";
                    if(i != data.page) {
                        href = 'href=javascript:go(' + i + ')';
                    }
                    setPaging(href, digit);
                }

                digit = '&nbsp;다음&nbsp;';
                href = "";
                if(data.page < data.maxpage) {
                    href = 'href=javascript:go(' + (data.page +1) +')';
                }
                setPaging(href, digit);

                $('.pagination').append(output)
            } // if(data.listcount) end

        }, // success end
        error : function () {
            console.log('에러')
        }
    })
}

$(function (){
    $("#viewcount").change(function (){
        go(1); // 보여줄 페이지를 1로 설정합니다.
    });

    $("button").click(function () {
        location.href="BoardWrite.bo";
    })
})