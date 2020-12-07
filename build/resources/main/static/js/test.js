var gameState = 0;
var dayCount = 0;

var voteT = 0;
var playerNum = 0;
var votedPlayer;
var livePlayer;
var choicePlayer;
var kingVotedPlayer;
var mafiaLen;
var citizenLen;

var role = {
    citizen: {
        jobName: 'citizen',
        group: 'citizen',

        live: true,
        ability: false,
        abilityFunc: function (chosenPlayer) {
        }

    },
    cop: {
        jobName: 'cop',
        group: 'citizen',

        live: true,
        ability: true,
        abilityFunc: function () {//서버로부터 선택할 플레이어정보 받아옴
            for (var i = 0; i < playerNum; i++) {
                $("#vote_" + i).on("click", function cop() { // 능력사용
                    choicePlayer = $(this).children()[1].outerText;
                    $.ajax({
                        url: `/cop/${roomNumber}`,
                        type: 'POST',
                        data: {
                            "choicePlayer": choicePlayer
                        },
                        dataType: 'json',
                        async: false,
                        success: function (data) {
                            alert(data.job);
                            if(data.job==='mafia'){
                                $(".chat_box").append("\n선택한 대상은 mafia가 맞습니다.");
                                $(".chat_box").scrollTop($(".chat_box")[0].scrollHeight);
                                for (var i = 0; i < playerNum; i++) {
                                    $("#vote_" + i).attr("onclick", '').unbind('click');
                                }
                            }
                            else{
                                $(".chat_box").append("\n선택한 대상은 mafia가 아닙니다.");
                                $(".chat_box").scrollTop($(".chat_box")[0].scrollHeight);
                                for (var i = 0; i < playerNum; i++) {
                                    $("#vote_" + i).attr("onclick", '').unbind('click');
                                }
                            }
                        }
                    });
                })
            }
        }
    },
    doctor: {
        jobName: 'doctor',
        group: 'citizen',
        live: true,
        ability: true,
        abilityFunc: function () {
            for (var i = 0; i < playerNum; i++) {
                $("#vote_" + i).on("click", function doctor() { // 능력사용
                    choicePlayer = $(this).children()[1].outerText;
                    $.ajax({
                        url: `/doctor/${roomNumber}`,
                        type: 'POST',
                        data: {
                            "choicePlayer": choicePlayer
                        },
                        dataType: 'json',
                        async: false,
                        success: function (data) {
                            for (var i = 0; i < playerNum; i++) {
                                $("#vote_" + i).attr("onclick", '').unbind('click');
                            }
                        }
                    });
                })
            }
        },

    },
    mafia: {
        jobName: 'mafia',
        group: 'mafia',
        live: true,
        ability: false
    },
    spy: {
        jobName: 'spy',
        live: true,
        group: 'citizen', // spy의 성격상 마피아를 지목하지 않으면 시민팀이기 때문에 처음엔 citizen으로 배정함
        ability: true,
        abilityFunc: function () {
            for (var i = 0; i < playerNum; i++) {
                $("#vote_" + i).on("click", function spy() { // 능력사용
                    choicePlayer = $(this).children()[1].outerText;
                    $.ajax({
                        url: `/spy/${roomNumber}`,
                        type: 'POST',
                        data: {
                            "srcPlayer" : username,
                            "choicePlayer": choicePlayer
                        },
                        dataType: 'json',
                        async: false,
                        success: function (data) {
                            if(data.job==='mafia'){
                                $(".chat_box").append("\n선택한 대상은 mafia가 맞습니다.");
                                $(".chat_box").scrollTop($(".chat_box")[0].scrollHeight);
                                for (var i = 0; i < playerNum; i++) {
                                    $("#vote_" + i).attr("onclick", '').unbind('click');
                                }
                                player=role['mafia'];
                                console.log(player);
                            }
                            else{
                                $(".chat_box").append("\n선택한 대상은 mafia가 아닙니다.");
                                $(".chat_box").scrollTop($(".chat_box")[0].scrollHeight);
                                for (var i = 0; i < playerNum; i++) {
                                    $("#vote_" + i).attr("onclick", '').unbind('click');
                                }
                            }
                        }
                    });
                })
            }
        }
    },
}
var playerRoles = [];
var userName;
var player = {
    jobName: 'null',
    group: 'null',
    live: true,
    ability: false, //구조는 나중에 건들기
};

function checkWin(){ //어느 진영이 승리하였는지에 관한 함수
    $.ajax({
        url: `/checkWin/${roomNumber}`,
        type: 'POST',
        success: function (data) {
            mafiaLen = data.mafiaLen;
            citizenLen = data.citizenLen;
            console.log(mafiaLen);
            console.log("===========================================================1");
            console.log(citizenLen);
            console.log("===========================================================1");
        }
    });
    if(mafiaLen!=0){
        if(livePlayer<=(playerNum/2)){
            if(citizenLen>mafiaLen){
                gameState=3;
                $(".chat_box").append("\n시민팀의 승리입니다.");
                $(".chat_box").scrollTop($(".chat_box")[0].scrollHeight);
                //시민승
            }
            else{
                gameState=3;
                $(".chat_box").append("\n마피아팀의 승리입니다.");
                $(".chat_box").scrollTop($(".chat_box")[0].scrollHeight);
                //마피아승
            }
        }
    }
    else{
        gameState=3;
        $(".chat_box").append("\n시민팀의 승리입니다.");
        $(".chat_box").scrollTop($(".chat_box")[0].scrollHeight);
        //시민승
    }
}

function dayTime() { //낮시간 진행시키는 함수

    if (gameState !== 3) {

        gameState = 1;
        dayCount++;
        $(".chat_box").append("\n" + dayCount + "일째 낮입니다. 3분동안 마피아를 찾아주세요");
        $(".chat_box").scrollTop($(".chat_box")[0].scrollHeight);
        //$("#jumb").css("background-image","url(https://cdn.pixabay.com/photo/2020/04/11/11/55/sun-5030147_960_720.jpg)");
        /* 낮화면을 출력해주는 css 스타일 변경 */
        setTimeout(function () {
            $(".chat_box").append("\n1분간 투표를 진행합니다 현재인원 리스트에서 마피아를 지목해주세요");
            $(".chat_box").scrollTop($(".chat_box")[0].scrollHeight);
            voteTime();
        }, 10000); // 3분뒤 투표실행
    }
}

function voteTime() {
    if (gameState === 1) {
        if (player.live === true) {

            for (var i = 0; i < playerNum; i++) {
                $("#vote_" + i).on("click", function vote() { // 투표와 관련된 함수
                    if (voteT == 0) { // 죽은사람은 권한이 없게 처리해야하는데,,, how?
                        votedPlayer = $(this).children()[1].outerText;
                        $.ajax({
                            url: `/vote/${roomNumber}`,
                            type: 'POST',
                            data: {
                                "votedPlayer": votedPlayer
                            },
                            dataType: 'json',
                            async: false,
                            success: function (data) {
                            }
                        });
                        voteT++;
                    } else {
                        alert("이미 투표를 하셨습니다.");
                    }
                })
            }

        } else {
            alert("투표 권한이 없습니다.");
        }

        setTimeout(function () {
            $.ajax({
                url: `/votefinal/${roomNumber}`,
                type: 'POST',
                dataType: 'json',
                async: false,
                success: function (data) {

                    kingVotedPlayer = data.kingVotedPlayer;
                    console.log(kingVotedPlayer);
                }
            });
            if (kingVotedPlayer === undefined) {
                $(".chat_box").append("\n투표수가 중복됫거나 아무도 투표를 하지 않았습니다. 바로 다음시간으로 넘어갑니다.");
                $(".chat_box").scrollTop($(".chat_box")[0].scrollHeight);
                voteT = 0;
                checkWin();
                nightTime();
            } else {
                diePlayer(kingVotedPlayer);
                voteT = 0;
                checkWin();
                nightTime();
            }
        }, 20000);
    } else if (gameState == 2) { //마피아만 투표를 한다는것.
        if (player.live === true) {
            for (var i = 0; i < playerNum; i++) {
                $("#vote_" + i).on("click", function vote() { // 투표와 관련된 함수
                    if (voteT == 0) { // 죽은사람은 권한이 없게 처리해야하는데,,, how?
                        votedPlayer = $(this).children()[1].outerText;
                        $.ajax({
                            url: `/maifavote/${roomNumber}`,
                            type: 'POST',
                            data: {
                                "votedPlayer": votedPlayer
                            },
                            dataType: 'json',
                            async: false,
                            success: function (data) {
                                alert("투표완료");
                            }

                        });
                        voteT++;
                    } else {
                        alert("이미 투표를 하셨습니다.");
                    }
                })
            }

        } else {
            alert("투표 권한이 없습니다.");
        }

    }


}

function diePlayer(Player) {
    $.ajax({
        url: `/kill/${roomNumber}`,
        type: 'POST',
        data: {
            "diePlayer": Player
        },
        success: function (data) {
            livePlayer = data.livePlayerLen;
            console.log(data.livePlayerLen);
        }
    });
    var $dieS = $(`<svg width="2em" height="2em" viewBox="0 0 16 16" class="bi bi-person" fill="currentColor"
                        xmlns="http://www.w3.org/2000/svg" style="color:black">
                        <path fill-rule="evenodd"
                            d="M13 14s1 0 1-1-1-4-6-4-6 3-6 4 1 1 1 1h10zm-9.995-.944v-.002.002zM3.022 13h9.956a.274.274 0 0 0 .014-.002l.008-.002c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664a1.05 1.05 0 0 0 .022.004zm9.974.056v-.002.002zM8 7a2 2 0 1 0 0-4 2 2 0 0 0 0 4zm3-2a3 3 0 1 1-6 0 3 3 0 0 1 6 0z" />
                    </svg>`);
    var $die = $(`<dd> 
        <svg width="1.5em" height="1.5em" viewBox="0 0 16 16" className="bi bi-person-fill"
             fill="currentColor" xmlns="http://www.w3.org/2000/svg">
            <path fill-rule="evenodd"
                  d="M13 14s1 0 1-1-1-4-6-4-6 3-6 4 1 1 1 1h10zm-9.995-.944v-.002.002zM3.022 13h9.956a.274.274 0 0 0 .014-.002l.008-.002c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664a1.05 1.05 0 0 0 .022.004zm9.974.056v-.002.002zM8 7a2 2 0 1 0 0-4 2 2 0 0 0 0 4zm3-2a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"/>
            <span className="mx-2">${Player}</span>
        </svg>
    </dd>`);
    for (var i = 1; i <= playerNum; i++) {
        if ($('#current').children()[i].innerText === Player) {
            console.log($('#current').children()[i].innerText);
            $('#current').children()[i].remove();
        }
    }
    $('#livet').children()[1].remove();
    $('#livet').append($dieS);
    $('#die').append($die);
    if (userName === Player) {
        for (var i = 0; i < playerNum; i++) {
            $("#vote_" + i).attr("onclick", '').unbind('click');
        }
        player.live = false;
    }

}

function nightTime() { //일반 시민들 스크립트 처리는 어떻게 할것인가,,?
    for (var i = 0; i < playerNum; i++) {
        $("#vote_" + i).attr("onclick", '').unbind('click');
    }
    if (gameState !== 3) {
        gameState = 2;
        $(".chat_box").append("\n밤이 되었습니다. 마피아들은 활동을 시작 할 것입니다.");
        $(".chat_box").scrollTop($(".chat_box")[0].scrollHeight);
        $("#jumb").css("background-image", "url(https://cdn.pixabay.com/photo/2019/05/24/13/34/planet-4226262_960_720.png)");
        /* 밤 화면을 출력해주는 코드 작성
                여기서는 서버로부터 플레이어의 직업과 그룹정보를 받아오는 코드도 작성 */
    }
    console.log("=====================================");
    console.log(player);
    console.log("=====================================");
    //채팅창 마피아만 쓸수있게해야됨
    if (player.live === true) {
        if (player.jobName === 'mafia') {
            $(".chat_box").append("\n죽일 대상을 선택해주세요.");
            $(".chat_box").scrollTop($(".chat_box")[0].scrollHeight);
            voteTime();
        } else if (player.ability === true) {
            player.abilityFunc();

        }
    }

    setTimeout(function () {
        $.ajax({
            url: `/mafiavotefinal/${roomNumber}`,
            type: 'POST',
            dataType: 'json',
            async: false,
            success: function (data) {
                kingVotedPlayer = data.kingVotedPlayer;
                console.log(kingVotedPlayer);
            }
        });
        if (kingVotedPlayer === undefined) {
            $(".chat_box").append("\n마피아의 살인은 없었습니다. 아무도 죽지 않았습니다.");
            $(".chat_box").scrollTop($(".chat_box")[0].scrollHeight);
            voteT = 0;
        }
        else if(kingVotedPlayer === 'save'){
            $(".chat_box").append("\n의사의 활약으로 아무도 죽지 않았습니다.");
            $(".chat_box").scrollTop($(".chat_box")[0].scrollHeight);
            voteT = 0;
        }
        else {
            diePlayer(kingVotedPlayer);
            voteT = 0;
            $(".chat_box").append("\n"+kingVotedPlayer+"가 마피아에 의해 살해당했습니다.");
            $(".chat_box").scrollTop($(".chat_box")[0].scrollHeight);
        }
        for (var i = 0; i < playerNum; i++) {
            $("#vote_" + i).attr("onclick", '').unbind('click');
        }
        checkWin();
        dayTime();
    }, 10000);

}


function defaultRole(playerNum) { //현재 플레이어 수에 따라 역할 배분하는 함수
    switch (playerNum) {
        case 1:
            playerRoles = [
                'doctor'
            ]
            return true;
        case 2:
            playerRoles = [
                'mafia',
                'spy'
            ]
            return true;
        case 3:
            playerRoles = [
                'mafia',
                'spy',
                'doctor'
            ]
            return true;
        case 4:
            playerRoles = [
                'citizen',
                'cop',
                'doctor',
                'mafia'
            ]
            return true;
        case 5:
            playerRoles = [
                'citizen',
                'cop',
                'doctor',
                'mafia',
                'spy'
            ]
            return true;
        case 6:
            playerRoles = [
                'citizen',
                'citizen',
                'cop',
                'doctor',
                'mafia',
                'mafia'
            ]
            return true;
        case 7:
            playerRoles = [
                'citizen',
                'citizen',
                'cop',
                'doctor',
                'mafia',
                'mafia',
                'spy'
            ]
            return true;
        case 8:
            playerRoles = [
                'citizen',
                'citizen',
                'citizen',
                'cop',
                'doctor',
                'mafia',
                'mafia',
                'spy'
            ]
            return true;
        default:
            return false;
    }

}

$('#start').on("click", function () {

    socket.send("/room/" + roomNumber + "/start", {},
        JSON.stringify({
                'msg': $(".chatsub").val(),
                'id': username,
                'roomid': roomNumber
            }
        ))

    //initGame();

});


function initGame() { //게임 실행 함수
    $.ajax({
        url: `/enter/${roomNumber}`,
        type: 'POST',
        data: {
            "roomNumber": roomNumber
        },
        dataType: 'json',
        async: false,
        success: function (data) {
            playerNum = data.playerNum;

        }
    });
    if (playerNum >= 1) {
        // alert("게임실행 완료");
        $(".chat_box").append("\n게임이 시작되었습니다.");
        $(".chat_box").scrollTop($(".chat_box")[0].scrollHeight);
        $('#start').attr('disabled', true);
        defaultRole(playerNum);
        //player = role['citizen'];
        $.ajax({
            url: `/assign/${roomNumber}`,
            type: 'POST',
            data: {
                "playerRoles": playerRoles
            },
            dataType: 'json',
            async: false,
            success: function (data) {
                for (var i = 0; i < playerNum; i++) {
                    if (data.players[i].userId == username) {
                        player = role[data.players[i].jobName];
                        userName = username;
                    }
                }
                console.log(player);
                console.log(userName);
            }
        });
        $(".chat_box").append("\n당신의 직업은 " + player.jobName + " 입니다.");
        dayTime();


    } else {
        alert("인원이 부족합니다");
    }
}