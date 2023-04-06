## Configuration
Change the `nl.jesper.songshare.song-files-dir` property in `src/main/resources/application.properties` to a existing directory where you want to store song binaries (this needs to be a absolute path).

Also make sure the database properties are set up correctly. These are the default datasource settings:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/songshare
spring.datasource.username=root
spring.datasource.password=
```

## Endpoints

### Register & retrieve token
`http://localhost:8080/users/register`

| HTTP Method | POST |
|-------------|------|
```json
{
"username": "testuser",
"password": "1234"
}
```

### Authenticate & retrieve token
`http://localhost:8080/users/login`

| HTTP Method | POST |
|-------------|------|
```json
{
"username": "testuser",
"password": "1234"
}
```

### Upload song _(authentication required)_
`http://localhost:8080/songs/upload`

| HTTP Method | POST |
|-------------|------|


```json
{
  "songFile": {
    "songBase64": "example_base64_encoded_song_file",
    "fileName": "example_song_file_name.mp3"
  },
  "songtitle": "example_song_title",
  "songartist": "example_song_artist"
}
```

You can generate a upload request using `makeUploadRequest.py --file=song.mp3`
```
python3 makeUploadRequest.py --file=black.mp3
Enter the song title: Black
Enter the song artist: Pearl Jam
```
This will base64 encode the file and make a correct upload request. Outputs to `request.json`
<br><br>Please make sure the file's contents is indeed a playable .mp3 file and not corrupted, or otherwise the API won't accept it.
The file extension can be anything as the server will add the .mp3 extension if it's not already there.

### Browse uploaded songs _(authentication required)_
`http://localhost:8080/songs`

| HTTP Method | GET |
|-------------|-----|
#### List all songs (ordered by uploadTimeStamp, and page size 10 by default)
```
No request body required
```

#### Sort by uploadTimeStamp (default)
```json
{
  "page": 0,
  "size": 10,
  "search": "pearl jam",
  "sort": "uploadTimeStamp",
  "order": "asc"
}
```
#### Sort by songArtist
```json
{
  "page": 0,
  "size": 10,
  "search": "pearl jam",
  "sort": "songArtist",
  "order": "asc"
}
```
#### Sort by songTitle
```json
{
  "page": 0,
  "size": 10,
  "search": "black",
  "sort": "songTitle",
  "order": "asc"
}
```

### Download song _(authentication required)_
`http://localhost:8080/songs/download`

| HTTP Method | GET |
|-------------|-----|
```json
{
  "songID": 1
}
```

### List your own uploaded songs _(authentication required)_
`http://localhost:8080/songs/myuploads`

| HTTP Method | GET |
|-------------|-----|
```
No request body required
```

### Delete one of your uploads _(authentication required)_
`http://localhost:8080/songs/delete`

| HTTP Method | DELETE |
|-------------|--------|

```json
{
  "songID": 1
}
```