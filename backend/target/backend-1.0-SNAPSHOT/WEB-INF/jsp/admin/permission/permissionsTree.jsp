<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="permission-tree"></div>
<script>
    (function (permissionTree) {
        var treeBuild = function(children,root){
            for(var j=0;j<children.length;j++){
                var item = children[j];
                var selected = item.selected?"checked class='has'":" class='not-has'";
                var li = $('<li><label><input type="${!empty type?type:'radio'}" name="model[\'parentId\']" '+selected+' value="'+item.id+'">'+item.name+'</label></li>');
                root.append(li);
                if(item.children&&item.children.length>0){
                    var cRoot = $("<ul/>");
                    li.append(cRoot);
                    treeBuild(item.children,cRoot);
                }
            }
        };
        var root = $("<ul/>");
        treeBuild(permissionTree,root);
        $(".permission-tree").append(root);
    })(${permissions});
</script>
