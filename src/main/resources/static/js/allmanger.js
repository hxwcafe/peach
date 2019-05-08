//个人全部信息的展示
function selsctuserallInfo() {
    $("#alluserInfo").on('show.bs.modal', function (event) {
        var btnThis = $(event.relatedTarget); //触发事件的按钮
        var modal = $(this);  //当前模态框
        var modalId = btnThis.data('id');   //解析出data-id的内容
        $.ajax({
            type: "get",
            dataType: 'JSON',
            url: path + "/mate/selectuamate",
            data: {"userId": modalId},
            success: function (data) {

                document.getElementById("userRealname").innerHTML=data.data.userRealname;
                document.getElementById("gender").innerHTML=pdallsex(data.data.gender);
                document.getElementById("userage").innerHTML=data.data.userage;
                document.getElementById("isMarriage").innerHTML=pdallhf(data.data.isMarriage);
                document.getElementById("userHeight").innerHTML=data.data.userHeight;
                document.getElementById("userEducation").innerHTML=data.data.userEducation;
                document.getElementById("userOccupation").innerHTML=data.data.userOccupation;
                document.getElementById("userSalary").innerHTML=data.data.userSalary;
                document.getElementById("userphone").innerHTML=data.data.userphone;
                document.getElementById("userBirthday").innerHTML=data.data.userBirthday;
                document.getElementById("usernative").innerHTML=data.data.usernative;
                document.getElementById("userMarrytime").innerHTML=data.data.userMarrytime;
                document.getElementById("userInterest").innerHTML=data.data.userInterest;
                document.getElementById("userHouse").innerHTML=pdalltf(data.data.userHouse);
                document.getElementById("userCar").innerHTML=pdalltf(data.data.userCar);

                document.getElementById("vipGrade").innerHTML=data.data.userVipl.vipGrade;
                document.getElementById("vipAppointment").innerHTML=data.data.userVipl.vipAppointment;
                document.getElementById("vipCreatetime").innerHTML=data.data.userVipl.vipCreatetime;
                document.getElementById("vipEndtime").innerHTML=data.data.userVipl.vipEndtime;
                document.getElementById("userWallet").innerHTML=data.data.userVipl.userWallet;
                document.getElementById("userDeposit").innerHTML=data.data.userVipl.userDeposit;
                document.getElementById("userFree").innerHTML=pdalltf(data.data.userVipl.userFree);
                //如果择偶要求没有数据不显示内容
                $('.zoyq').css({"display":"block"});
                if(data.data.mate!=null) {
                    document.getElementById("mateAge").innerHTML = data.data.mate.mateAge;
                    document.getElementById("mateHeight").innerHTML = data.data.mate.mateHeight;
                    document.getElementById("mateSalary").innerHTML = data.data.mate.mateSalary;
                    document.getElementById("mateEducation").innerHTML = data.data.mate.mateEducation;
                    document.getElementById("mateMarrytime").innerHTML = data.data.mate.mateMarrytime;
                    document.getElementById("mateNative").innerHTML = data.data.mate.mateNative;
                    document.getElementById("mateMarriage").innerHTML = data.data.mate.mateMarriage;
                    document.getElementById("mateBelief").innerHTML = data.data.mate.mateBelief;
                    document.getElementById("mateDrink").innerHTML = data.data.mate.mateDrink;
                    document.getElementById("mateSmoking").innerHTML = data.data.mate.mateSmoking;
                    document.getElementById("isChildren").innerHTML = data.data.mate.isChildren;
                }else {
                    $('.zoyq').css({"display":"none"});
                }
            }, error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert("出现错误!!");
            }
        });
    })
}
function pdallsex(value) {
    if (value==1){
        return "男";
    }else if (value==2) {
        return "女";
    }else if(value==0){
        return "未知";
    }
}
function pdallhf(value) {
    if (value == 1) {
        return "离异";
    } else {
        return "未婚";
    }
}
function pdalltf(value){
    if(value==1){
        return "是";
    }else{
        return "否";
    }
}
function appointmentusermy(value,row){
    var id = row.myid;
    var result = "";
    var name = pduserRname(row.myuser.userRealname);
    result +="<div style=\"margin:auto 0;\">";
    result += "<a class='btn btn-xs blue' data-toggle=\"modal\" data-target=\"#alluserInfo\" data-id=\""+id+"\">"+name+"</a>";
    result +="</div>";
    return result;
}
function appointmentuseryou(value,row){
    var id = row.youid;
    var result = "";
    var name = pduserRname(row.youuser.userRealname);
    result +="<div style=\"margin:auto 0;\">";
    result += "<a class='btn btn-xs blue' data-toggle=\"modal\" data-target=\"#alluserInfo\" data-id=\""+id+"\">"+name+"</a>";
    result +="</div>";
    return result;
}
function pduserRname(value) {
    var name = value;
    if(value==null||value==""){
        name = "没有填写真实姓名";
    }
    return name;
}