## user

recifoteca user create "<user.name>" <user.email> <user.type>[aluno, servidor, visitante]

recifoteca user list
  flags:
    -n <user.name> filter by user name
    -m <user.email> filter by user email
    -t <user.type> filter by user type
    (display in json)

recifoteca user delete [user.email]

recifoteca user update user.email "<user.name>" <user.email> <user.type>[aluno, servidor, visitante]

recifoteca user info [user.email]

recifoteca user borrow history [user.email]
  returns the history of borrowed items for a specific user, detail the amount of times a late fee has been applied

## mídia (items que podem ser emprestados)

recifoteca media create "<media.name>" <media.type>[livro, revista, digital] <media.fine.per.day>
  (this generates a media.id and returns it)
  (digital media should have no stock)

recifoteca media list
  flags:
    -n <media.name> filter by media name
    -t <media.type> filter by media type
  (this returns with the media id)
  (this returns the media info + the amount of borrowed items and amount of available items)
  (display in json)

recifoteca media info <media.id>

recifoteca media delete  <media.id>

### mídia stock

recifoteca media units add <media.id> amount
  (this adds more units to the stock of this specific media)
  (digital media should have no stock)

recifoteca media units remove <media.id> amount
  (this removes units from the stock of this specific media)
  (it should check if there are enough available media units to be removed)
  (digital media should have no stock)

### mídia borrow

recifoteca media borrow <media.id> <user.email> <amount>
  flags:
    -d <borrow.date> date that the media was borrowed (defaults to today)
    -u <borrow.until> date that the borrowed media is estimated to be returned (defaults to today + 7 days)
    the borrow date follows the format DD/MM/YYY
    the borrow until follows the format DD/MM/YYY
    (obviously borrow.until > borrow.date)
  (this registers that a user has borrows x Amount of units of this specific media)

recifoteca media return <media.id> <user.email> <amount>
 flags:
    -d <return.date> date that the media was returned (defaults to today)
    the return date follows the format DD/MM/YYYY
  (this registers that a user has returned x Amount of units of this specific media)
  (if the user has borrowed for longer than allowed, calculate the fines)

recifoteca media borrow list
  flags:
    -n <media.name> filter by media name
    -t <media.type> filter by media type
    -l filter only media borrows that are late

  (display in json)
