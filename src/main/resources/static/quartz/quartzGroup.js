layui.use(['form','layer','table'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        addform = "";

    var tableIns = table.render({
        id : "scheduleJobGroupId",
        elem: '#groupList',
        url : '/quartz/query/quartz/scheduleJobGroupList',
        cellMinWidth : 100,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        id : "groupList",
        cols : [[
            {field: 'scheduleJobGroupName', title: '分组名称', minWidth:100, align:"center"},
            {field: 'scheduleJobGroupDescription', title: '分组说明', minWidth:200,align:'center'},
            {field: 'status', title: '分组状态', minWidth:100, align:'center',templet:function(d){
                if(d.status == "0"){
                    return "运行中";
                }else if(d.status == "1"){
                    return "停止";
                }else{
                    return "未知状态";
                }
            }},
            {field: 'createTime', title: '创建时间',minWidth:100, align:'center'},
            {title: '操作', minWidth:175, templet:'#groupListBar',fixed:"right",align:"center"}
        ]]
    });

    $(".addGroup").click(function(){
        addGroup();
    })

    function addGroup(edit){
        addform = layer.open({
            title : "编辑任务分组",
            type : 1,
            skin: 'layui-layer-rim', //加上边框
            area: ['80%', '85%'], //宽高
            closeBtn: 1,
            resize: false,
            shadeClose: false,//开启遮罩
            content : $(".groupForm"),
            success : function(layero, index){
                $(".groupForm").removeClass("hidecustom");
                $(".resetFrom").trigger("click");
                if(edit){
                    $(".idval").val(edit.scheduleJobGroupId);
                    $(".scheduleJobGroupName").val(edit.scheduleJobGroupName);
                    $(".scheduleJobGroupDescription").val(edit.scheduleJobGroupDescription);
                    $(".status").prop("checked",edit.status==1?'checked':'');
                    form.render();
                }
            }
        })
    }

    table.on('tool(groupList)', function(obj){
        var layEvent = obj.event, data = obj.data;
        if(layEvent === 'edit'){
            addGroup(data);
        } else if(layEvent === 'usable'){
            var _this=$(this),usableText = "是否确定运行该分组任务？",url = "/quartz/start/quartz/schedulejobGroup";
            if(data.status==0){
                usableText = "是否确定停止该分组任务？",url = "/quartz/stop/quartz/schedulejobGroup";
            }
            layer.confirm(usableText,{
                icon: 3,
                title:'温馨提示',
                cancel : function(index){
                    layer.close(index);
                }
            },function(index){
                layer.close(index);
                $.ajax({
                    url : url,
                    type : "post",
                    dataType : "json",
                    data: {scheduleJobGroupId:data.scheduleJobGroupId},
                    success : function(data){
                        if(data.code==0){
                            layer.msg(data.data);
                            layer.close(index);
                            tableIns.reload();
                        }else{
                            layer.msg(data.error);
                        }
                    }
                })
            },function(index){
                layer.close(index);
            });
        } else if(layEvent === 'del'){
            layer.confirm('确定删除任务分组？',{icon:3, title:'温馨提示'},function(index){
                $.ajax({
                    url : "/quartz/del/quartz/schedulejobGroup",
                    type : "post",
                    dataType : "json",
                    data: {scheduleJobGroupId:data.scheduleJobGroupId},
                    success : function(data){
                        if(data.code==0){
                            layer.msg(data.data);
                            layer.close(index);
                            tableIns.reload();
                        }else{
                            layer.msg(data.error);
                        }
                    }
                })
            });
        }
    });

    $(".modifyQuartzGroup").click(function () {
        var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        var url = "/quartz/add/quartz/schedulejobGroup";
        if ($(".idval").val()!=null&&$(".idval").val()!=undefined&&$(".idval").val()!="") {
            url = "/quartz/edit/quartz/scheduleJobGroup";
        }
        $.ajax({
            url : url,
            type : "post",
            data:$(".groupForm").serialize(),
            dataType : "json",
            success : function(data){
                if(data.code==0){
                    layer.msg(data.data);
                    setTimeout(function () {
                        layer.close(index);
                        layer.close(addform);
                        $(".groupForm").addClass("hidecustom");
                        window.location.reload();
                    }, 1000);
                }else{
                    layer.msg(data.error);
                }
            }
        })
        return false;
    })

})