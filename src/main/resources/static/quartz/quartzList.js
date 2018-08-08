layui.use(['form','layer','table'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        addform = "";

    $.ajax({
        url : "/quartz/query/quartz/scheduleJobGroupAll",
        type : "get",
        cache:true,
        dataType : "json",
        success : function(data){
            if(data.code==0){
                var categoryHtml = '<option value="-1">全部</option>';
                $.each(data.data,function(i,v){
                    categoryHtml += '<option value="'+v.scheduleJobGroupId+'">'+v.scheduleJobGroupName+'</option>';
                });
                $("#search").append(categoryHtml);
                form.render('select');
            }
        }
    })

    var tableIns = table.render({
        id : "scheduleJobId",
        elem: '#quartzList',
        url : '/quartz/query/quartz/schedulejobList',
        cellMinWidth : 100,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        id : "quartzList",
        cols : [[
            {field: 'scheduleJobName', title: '定时器名称', minWidth:100, align:"center"},
            {field: 'scheduleJobCronExpression', title: '表达式', minWidth:200,align:'center'},
            {field: 'scheduleJobClass', title: '执行类全路径', minWidth:100, align:"center"},
            {field: 'scheduleJobMethod', title: '执行方法', minWidth:200,align:'center'},
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
            {field: 'quartzSchedulejobGroup.scheduleJobGroupName', title:'分组类型',width:150,align:"center",templet:function(d){
                return d.quartzSchedulejobGroup.scheduleJobGroupName;
            }},
            {title: '操作', minWidth:175, templet:'#quartzListBar',fixed:"right",align:"center"}
        ]]
    });

    $(".addQuartz").click(function(){
        addQuartz();
    })

    function addQuartz(edit){
        addform = layer.open({
            title : "编辑任务",
            type : 1,
            skin: 'layui-layer-rim', //加上边框
            area: ['80%', '85%'], //宽高
            closeBtn: 1,
            resize: false,
            shadeClose: false,//开启遮罩
            content : $(".quartzForm"),
            success : function(layero, index){
                $(".quartzForm").removeClass("hidecustom");
                $(".resetFrom").trigger("click");
                if(edit){
                    $(".idval").val(edit.scheduleJobId);
                    $(".scheduleJobName").val(edit.scheduleJobName);
                    $(".scheduleJobCronExpression").val(edit.scheduleJobCronExpression);
                    $(".scheduleJobClass").val(edit.scheduleJobClass);
                    $(".scheduleJobMethod").val(edit.scheduleJobMethod);
                    $(".scheduleJobDescription").val(edit.scheduleJobDescription);
                    $(".status").prop("checked",edit.status==1?'checked':'');
                    form.render();
                }
            }
        })
    }

    $(".syncQuartz").click(function(){
        layer.confirm('确定同步所有任务？',{icon:3, title:'温馨提示'},function(index){
            $.ajax({
                url : "/quartz/async/quartz/schedulejob",
                type : "post",
                dataType : "json",
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
    })

    table.on('tool(quartzList)', function(obj){
        var layEvent = obj.event, data = obj.data;
        if(layEvent === 'edit'){
            addQuartz(data);
        } else if(layEvent === 'usable'){
            var _this=$(this),usableText = "是否确定运行该任务？",url = "/quartz/start/quartz/schedulejob";
            if(data.status==0){
                usableText = "是否确定停止该任务？",url = "/quartz/stop/quartz/schedulejob";
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
                    data: {scheduleJobId:data.scheduleJobId},
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
            layer.confirm('确定删除任务？',{icon:3, title:'温馨提示'},function(index){
                $.ajax({
                    url : "/quartz/del/quartz/schedulejob",
                    type : "post",
                    dataType : "json",
                    data: {scheduleJobId:data.scheduleJobId},
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
        } else if(layEvent === 'reboot'){
            layer.confirm('确定重启任务？',{icon:3, title:'温馨提示'},function(index){
                $.ajax({
                    url : "/quartz/reboot/quartz/schedulejob",
                    type : "post",
                    dataType : "json",
                    data: {scheduleJobId:data.scheduleJobId},
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

    $(".modifyQuartz").click(function () {
        var group = $("#search").val();
        if ( group == null || group == undefined || group == -1 ) {
            layer.msg("任务组还没有选择哦！");
            return false;
        }
        var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        var url = "/quartz/add/quartz/schedulejob";
        if ($(".idval").val()!=null&&$(".idval").val()!=undefined&&$(".idval").val()!="") {
            url = "/quartz/edit/quartz/schedulejob";
        }
        $.ajax({
            url : url,
            type : "post",
            data:$(".quartzForm").serialize(),
            dataType : "json",
            success : function(data){
                if(data.code==0){
                    layer.msg(data.data);
                    setTimeout(function () {
                        layer.close(index);
                        layer.close(addform);
                        $(".quartzForm").addClass("hidecustom");
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