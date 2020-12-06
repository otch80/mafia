var gameState = 0;
var dayCount = 0;
var voteT =0;
var playerNum=0;
var votedPlayer;
var kingVotedPlayer;
var role = {
    citizen: {
        jobName: 'citizen',
        group: 'citizen',
        ability: false,
        abilityFunc: function (chosenPlayer){}
    },
    cop: {
        jobName: 'cop',
        group: 'citizen',
        ability: true,
        abilityFunc: function (chosenPlayer) {//서버로부터 선택할 플레이어정보 받아옴
            if (chosenPlayer.status == 1/* status가 1이면 생존 */) {
                if (chosenPlayer.name == 'mafia') { //지목한 플레이어의 상태가 마피아라면
                    showMafia(1);
                    //화면상에 마피아임을 알려주는 애니메이션 실행
                }
                else {
                    showMafia(0);
                    //화면상에 마피아가 아님을 알려주는 애니메이션 실행
                }
            }
            else {
                alert("현재 생존해있지 않습니다.");
                //현재 플레이어가 생존해있지 않음을 알려주는 애니메이션 실행
            }
        }
    },
    doctor: {
        jobName: 'doctor',
        group: 'citizen',
        ability: true,
        abilityFunc: function (chosenPlayer) {//서버로 부터 플레이어정보 받아옴
            if (chosenPlayer.status == 1/*지목한 플레이어가 죽었는가? 아니라면 실행*/) {
                chosenPlayer.imperishability = true; //플레이어의 상태를 무적으로 바꾸어 마피아로 부터 보호
            }
            else {
                alert("현재 생존해있지 않습니다.");
                //현재 플레이어가 생존해있지 않음을 알려주는 애니메이션 실행
            }
        }
    },
    priest: {
        jobName: 'priest',
        group: 'citizen',
        ability: false, // 성직자의 능력은 함수로 구현하지 않고 상태변화로 구현
        barrier : true
    },
    mafia: {
        jobName: 'mafia',
        group: 'mafia',
        ability: false // 마피아능력은 투표를 통해 스크립트로 구현해야되기 때문에 false로 함 만약, 나중에 필요하다면 true로 구현 할 예정
    },
    spy: {
        jobName: 'spy',
        group: 'citizen', // spy의 성격상 마피아를 지목하지 않으면 시민팀이기 때문에 처음엔 citizen으로 배정함
        ability: true,
        abilityFunc: function (chosenPlayer) {
            if (chosenPlayer.status == 1/*지목한 플레이어가 죽었는가? 아니라면 실행*/) {
                if (chosenPlayer.name == 'mafia') {//선택한 플레이어의 이름이 마피아가 맞으면 실행
                    this.group = 'mafia' // 현재플레이어는 마피아그룹이됨 이부분코드는 나중에 손봐줘야함
                    alert("당신은 이제부터 마피아입니다.");
                }
            }
            else {
                alert("현재 생존해있지 않습니다.");
                //현재 플레이어가 생존해있지 않음을 알려주는 애니메이션 실행
            }
        }
    },
    showMafia(num) {
        if(num==1){
            alert("마피아가 맞습니다.");
        }
        else{
            alert("마피아가 아닙니다.");
        }

    }
}
var playerRoles=[];
var players = [];
var userName;
var player = {
    jobName: 'null',
    group: 'null',
    ability: false, //구조는 나중에 건들기
};

function dayTime(){ //낮시간 진행시키는 함수
    if(gameState !== 4){
        gameState = 1;
        dayCount++;
        $(".chat_box").append("\n" +dayCount +"일째 낮입니다. 3분동안 마피아를 찾아주세요");
        $(".chat_box").scrollTop($(".chat_box")[0].scrollHeight);
        //$("#jumb").css("background-image","url(https://cdn.pixabay.com/photo/2020/04/11/11/55/sun-5030147_960_720.jpg)");
        /* 낮화면을 출력해주는 css 스타일 변경 */
        setTimeout(function(){
            $(".chat_box").append("\n1분간 투표를 진행합니다 현재인원 리스트에서 마피아를 지목해주세요");
            $(".chat_box").scrollTop($(".chat_box")[0].scrollHeight);
            voteTime();},5000); // 3분뒤 투표실행
    }
}

function voteTime(){
    for(var i=0;i<playerNum;i++){
        $("#vote_"+i).on("click", function vote() { // 투표와 관련된 함수
            if (voteT == 0) { // 죽은사람은 권한이 없게 처리해야하는데,,, how?
                votedPlayer = $(this).children()[1].outerText;
                $.ajax({
                    url:`/vote/${roomNumber}`,
                    type:'POST',
                    data : {
                        "votedPlayer" : votedPlayer
                    },
                    dataType:'json',
                    async:false,
                    success:function(data){
                    }
                });
                voteT++;
            }
            else{
                alert("이미 투표를 하셨습니다.");
            }
        })
    }

        setTimeout(function(){
            $.ajax({
                url:`/votefinal/${roomNumber}`,
                type:'POST',
                dataType:'json',
                async:false,
                success:function(data){
                    kingVotedPlayer = data.kingVotedPlayer;
                    console.log(kingVotedPlayer);
                }
            });
        },5000);




}

function vote() { // 투표와 관련된 함수
    if (voteT == 0) { // 죽은사람은 권한이 없게 처리해야하는데,,, how?
        votedPlayer = $(this).children()[1].outerText;
        $.ajax({
            url:`/vote/${roomNumber}`,
            type:'POST',
            data : {
                "votedPlayer" : votedPlayer
            },
            dataType:'json',
            async:false,
            success:function(data){
                kingVotedPlayer = data.kingVotedPlayer;
                console.log(kingVotedPlayer);

            }
        });
        voteT++;
    }
    else{
        alert("이미 투표를 하셨습니다.");
    }
}

function nightTime(){ //일반 시민들 스크립트 처리는 어떻게 할것인가,,?
    $("#vote").attr('onclick','').unbind('click');
    if(gameState !== 4){
        gameState = 3;
        $(".chat_box").append("\n밤이 되었습니다. 마피아들은 활동을 시작 할 것입니다.");
        $(".chat_box").scrollTop($(".chat_box")[0].scrollHeight);
        $("#jumb").css("background-image","url(https://cdn.pixabay.com/photo/2019/05/24/13/34/planet-4226262_960_720.png)");
        /* 밤 화면을 출력해주는 코드 작성
                여기서는 서버로부터 플레이어의 직업과 그룹정보를 받아오는 코드도 작성 */
    }
    //채팅창 마피아만 쓸수있게해야됨

}

        /*$.ajax({
            url:"컨트롤러 url",
            type:'GET',
            dataType:'json',
            async:false,
            success:function(data){
                livePlayers = data.livePlayers;
            }
        });
        $.('button').click(function(){
            $.ajax({
                url:"컨트롤러 url",
                type:'POST',
                data: JSON.stringify({//search["email"] = $("#email").val();
                    "votedPlayer" : //HTML내 이름 속성값을 참고하여 선정
                })
            });
        });*/
        /* 서버로부터 살아있는 사용자들의 목록을 받아온 뒤,
                사용자들이 서로 투표할 수 있게하는 코드작성 여기서
                가장 많은 표를 받은 플레이어는 votedPlayer가 됨*/
        /* setTimeout(function(){
            $.ajax({
                url:"컨트롤러 url",
                type:'GET',
                dataType:'json',
                async:false,
                success:function(data){
                    votedPlayer = data.votedPlayer;
                }
            }); //string 값으로 변환 필요
            if(votedPlayer){ // 만약 과반수 이상의 표를 받은 플레이어가 있다면
                executationTime(); // 처형의 시간으로 이동
            }
            else{
                nightTime(); // 밤으로 이동
            }
        },60000); */
function defaultRole(playerNum){ //현재 플레이어 수에 따라 역할 배분하는 함수
    switch(playerNum){
        case 1:
            playerRoles = [
                'doctor'
            ]
            return true;
        case 2:
            playerRoles = [
                'citizen',
                'cop'
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
                'priest',
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

$('#start').on("click",function(){

    socket.send("/room/"+roomNumber+"/start",{},
        JSON.stringify({
            'msg': $(".chatsub").val(),
            'id' : username,
            'roomid' : roomNumber}
        ))

     //initGame();

});


function initGame(){ //게임 실행 함수
        $.ajax({
            url:`/enter/${roomNumber}`,
            type:'POST',
            data : {
                "roomNumber" : roomNumber
            },
            dataType:'json',
            async:false,
            success:function(data){
                playerNum = data.playerNum;

            }
        });
        if(playerNum >= 1){
            // alert("게임실행 완료");
            $(".chat_box").append("\n게임이 시작되었습니다.");
            $(".chat_box").scrollTop($(".chat_box")[0].scrollHeight);
            $('#start').attr('disabled',true);
            defaultRole(playerNum);
            //player = role['citizen'];
            $.ajax({
                url:`/assign/${roomNumber}`,
                type:'POST',
                data : {
                    "playerRoles" : playerRoles
                },
                dataType:'json',
                async:false,
                success:function(data){
                    for(var i=0;i<playerNum;i++)
                    {
                        if(data.players[i].userId == username) {
                            player = role[data.players[i].jobName];
                            userName = username;
                        }
                    }
                    console.log(player);
                    console.log(userName);
                }
            });
            $(".chat_box").append("\n당신의 직업은 "+player.jobName+" 입니다.");
            dayTime();


        }
        else{
            alert("인원이 부족합니다");
        }

}