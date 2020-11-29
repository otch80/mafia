var playerNum=0;

$('#start').on("click",function(){
    initGame();
});


function initGame(){
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
                console.log(data);

            }
        });
        if(playerNum >= 2){
            alert("게임실행 완료");
        }
        else{
            alert("인원이 부족합니다");
        }
    /* if(defaultRole(players.length)==false){ // 이부분을 그냥 day.html에서 판단하게 넘길까 생각중
         현재 플레이어 인원수가 부족하다는 상태메시지 출력
        alert("현재 인원 부족");
        break;
    }
    assignRole();
    dayTime(0); */
}