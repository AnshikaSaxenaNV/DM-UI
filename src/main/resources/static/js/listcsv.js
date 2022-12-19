// read csv file on drop down
$(document).ready(function () {
    $('#entitylist').change(function () {
        var selectedFileName = $(this).children("option:selected").val();
            $.ajax({
                url: "/api/read/"+selectedFileName,
                dataType: "json",
                success: function (data) {
                    // console.log(data);
                    table_data = `<table>`;
                    for (const key in data) {
                        if (data.hasOwnProperty(key)) {
                            table_data+=`<tr>`;
                            table_data+=`<td>`+ `${key}`+`</td>`;
                            table_data+=`<td>`+`<input type="text"`+ `value="`+`${data[key]}"`+`>`+`</td>`;
                            table_data+=`</tr>`
                     //     console.log(`${key}: ${data[key]}`);
                        }
                    }
                    table_data+=`</table>`;
                    document.getElementById("csv_data_onselect").innerHTML = table_data;
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log("AJAX Error: " + textStatus + ' : ' + errorThrown);
                },
            });
    });
});
//read list of CSV files on drop down
$(document).ready(function () {
    $('#entitylist').change(function () {
        var selectedFileName = $(this).children("option:selected").val();
            $.ajax({
                url: "/api/readlist/"+selectedFileName,
                dataType: "json",
                success: function (data) {
                   // console.log(data);
                        table_data = `<table>`;
                        data.forEach(d => {
                        table_data += `<tr align="left"><th>Look_Field</th><th>Value</th></tr>`;
                        for (let key in d) {
                           table_data+=`<tr>`;
                           table_data+=`<td>`+ `${key}`+`</td>`;
                              // console.log(`${key}: ${d[key]}`+" ")
                               table_data+=`<td>`+`<input type="text"`+ `value="`+`${d[key]}"`+`>`+`</td>`;
                               table_data+=`</tr>`;
                              }
                              table_data+=`<tr><td></td><td></td></tr>`;
                              table_data+=`<tr><td></td><td></td></tr>`;
                               table_data+=`<tr><td></td><td></td></tr>`;
                              table_data+=`<tr><td></td><td></td></tr>`;
                              table_data+=`<tr><td></td><td></td></tr>`;
                               table_data+=`<tr><td></td><td></td></tr>`;
                              table_data+=`<tr><td></td><td></td></tr>`;
                              table_data+=`<tr><td></td><td></td></tr>`;
                         })
                        table_data+=`</table>`;
                        document.getElementById("list_all_data").innerHTML = table_data;
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log("AJAX Error: " + textStatus + ' : ' + errorThrown);
                },
            });
    });
});

//download
$(document).ready(function () {
    $('#entity').change(function () {
        var selectedFileName = $(this).children("option:selected").val();
        var loc = "http://localhost:8080/api/download";
        document.getElementById("download").setAttribute("href", loc + "/" + selectedFileName);
    });
});