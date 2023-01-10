//download
$(document).ready(function () {
    $('#entity').change(function () {
        var selectedFileName = $(this).children("option:selected").val();
        var loc = "http://localhost:8080/api/download";
        document.getElementById("download").setAttribute("href", loc + "/" + selectedFileName);
    });
});

//select admin primary entity
$(document).ready(function () {
    $('#entitylist').change(function () {
        var selectedFileName = $(this).children("option:selected").val();
        $.ajax({
            url : '/api/add',
            type : 'POST',
            data : selectedFileName,
            success : function(returndata) {
            console.log(selectedFileName);
            setInterval('location.reload()',100); 
            }
        })
    })
});

$(document).ready(function(){

    $('#setPrimary').fadeOut(5000);

    });

//selecting validate entity
$(document).ready(function () {
    $('#entityValidate').change(function () {
        var selectedFileName = $(this).children("option:selected").val();
        $.ajax({
            url : '/api/validate',
            type : 'POST',
            data : selectedFileName,
            success : function(returndata) {
            console.log(selectedFileName);
            setInterval('location.reload()',100); 
            
            }
        })
    })
});

//select validate Secondary entity
// $(document).ready(function () {
//     $('#entityValidateS').change(function () {
//         var selectedFileName = $(this).children("option:selected").val();
//         $.ajax({
//             url : '/api/validate',
//             type : 'POST',
//             data : selectedFileName,
//             success : function(returndata) {
//             console.log(selectedFileName);
            
//             }
//         })
//     })
// });

$(document).ready(function(){

    $('#mybtn-para').fadeOut(5000);

    });

//validate global lookup
function validateForm()
        {
           var inputs = document.getElementsByTagName("input");
           for(var i = 0; i < inputs.length; i++)
             {
                 if(inputs[i].type == "text")
                   {
                        var x = inputs[i].value;
                        if(inputs[i].value == "" || inputs[0].value == "" || inputs[1].value == "" || inputs[2].value == "" || inputs[3].value == "")
                          {
                             //alert("Field should not be empty");
                                     swal({
                                              //title: "Title",
                                                text: "Field should not be empty",
                                                timer: 3000,
                                          });
                            // document.getElementById("issue").innerHTML = "Field should not be empty";
                             return false;
                          }
                          else
                          {
                             return true;
                          }
                   }
              }
             //return true;
        }
// $(document).ready(function () {
//     $('#add').click(function () {
//         var loc = document.getElementById("newdata");
//     loc.innerHTML = "<tr id='nw'><td><input type='text' id='1'/></td><td><input type='text' id='1'/></td></tr>";
//     });
// });
// deleteRow
function deleteRow(el, sform) {
    swal({
        title: "Are you sure?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
    .then((willDelete) => {
        if (willDelete) {
            swal("Your record has been deleted!", {
                icon: "success",
            });
            var tbl = el.parentNode.parentNode.parentNode;
            var row = el.parentNode.parentNode.rowIndex;
            tbl.deleteRow(row);
            const formData = new FormData(sform);
            const formMap = new Map();
    for (var [key,value] of formData) {
        formMap.set(key, value);

    }
    console.log(formMap);
            $.ajax({
            url : '/api/writeS',
            type : 'POST',
            data : Object.fromEntries(formMap),
            dataType:"json",
            success : function(formMap) {
            console.log(formMap);
            //setInterval('location.reload()',100);
            window.location = '/admin';
            }
        })
        } else {
            swal("Your record is safe!");
        }
    });
    return false;
    
}
//add row
function addRow(id,i) {
    var t = document.getElementById(id);
  var rows = t.getElementsByTagName("tr");
  var r = rows[rows.length - 1];
  r.parentNode.insertBefore(getTemplateRow(i),r);
  }
  function getTemplateRow(i) {
   var x = document.getElementById(i).cloneNode(true);
   x.childNodes[0].nextSibling.firstChild.value="";
   x.childNodes[0].nextSibling.firstChild.name=i;
   x.childNodes[0].nextElementSibling.nextElementSibling.firstChild.value="";
   x.childNodes[0].nextElementSibling.nextElementSibling.firstChild.name=i+"val";
   console.log(x.childNodes);
   return x;
 }