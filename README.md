<a name="top"></a>
# ExcelParsing v1.0.0

Problem : How to export and import reports (excel) from / to the database and make sure there is no java heapspace memory errors

# Table of contents

- [Upload](#Upload)
- [Download](#Download)

___


# <a name='Upload'></a> Upload

<p>Upload file with xlsx and xls extensions and insert them into the database</p>

```
POST /api/v1/upload
```
### Parameters - `Request Body Parameters`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| file | `Multipart` | <p>excel file to be imported to database</p> |

### Excel File Sample
![Screenshot](https://i.ibb.co/0CpYC39/Untitled.png)

### Database Structure
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

<p>download data from database in excel file form</p>

```
GET /api/v1/download
```

### Sample Response
![Screenshot](https://i.ibb.co/rFH0DSx/Untitleasd.png)

