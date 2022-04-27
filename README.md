<a name="top"></a>
# ExcelParsing v1.0.0

Problem: How to export/import Microsoft Excel (.xlsx) file from/to the database and make sure there is no Java heap space memory error.

# Table of Contents

- [Upload](#Upload)
- [Download](#Download)

___


# <a name='Upload'></a> Upload

<p>Upload a Microsoft Excel file with .xlsx format and insert them into the database.</p>

```
POST /api/v1/upload
```
### Parameters - `Request Body Parameters`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| file | `Multipart` | <p>Microsoft Excel (.xlsx) file to be imported to the database</p> |

### Excel File Sample
![Screenshot](https://i.ibb.co/0CpYC39/Untitled.png)

### Table Structure
| Name     | Type       |
|----------|------------|
| id | `Varchar(50)` |
| full_name | `Varchar(255)` |
| birth_date | `Timestamp` |
| birth_place | `Varchar(100)` |
| address | `Varchar(255)` |
| phone_number | `Varchar(50)` |
| gender | `Varchar(50)` |

### Sample Response

  ```
    {
        "message": "Uploaded the file successfully: MOCK_DATA (2).xlsx!"
    }
  ```


# <a name='Download'></a> Download

<p>Download the data from the database in Microsoft Excel (.xlsx) file format.</p>

```
GET /api/v1/download
```

### Sample Response
![Screenshot](https://i.ibb.co/rFH0DSx/Untitleasd.png)

