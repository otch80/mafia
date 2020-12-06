var role = {
    citizen: {
        name: 'citizen',
        group: 'citizen',
        ability: false
    },
    cop: {
        name: 'cop',
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
        name: 'doctor',
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
        name: 'priest',
        group: 'citizen',
        ability: false, // 성직자의 능력은 함수로 구현하지 않고 상태변화로 구현
        barrier : true
    },
    mafia: {
        name: 'mafia',
        group: 'mafia',
        ability: false // 마피아능력은 투표를 통해 스크립트로 구현해야되기 때문에 false로 함 만약, 나중에 필요하다면 true로 구현 할 예정
    },
    spy: {
        name: 'spy',
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

export { role };