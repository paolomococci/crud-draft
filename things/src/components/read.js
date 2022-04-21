import axios from 'axios'
import {
  React,
  useEffect,
  useState
} from 'react'
import {
  Table,
  Button
} from 'semantic-ui-react'
import {
  Link
} from 'react-router-dom'

function Read() {

  const [
    APIData,
    setAPIData
  ] = useState([])
  useEffect(
    () => {
      axios.get(`http://localhost/sampledata`).then(
        (response) => {
          setAPIData(response.data)
        }
      )
    }
  )

  const setData = (data) => {
    let {
      id,
      name,
      surname,
      checked
    } = data
    localStorage.setItem('ID', id)
    localStorage.setItem('Name', name)
    localStorage.setItem('Surname', surname)
    localStorage.setItem('Checked', checked)
  }

  const getData = () => {
    axios.get(`http://localhost/sampledata`).then(
      (getData) => {
        setAPIData(getData.data)
      }
    )
  }

  const onDelete = (id) => {
    axios.delete(`http://localhost/sampledata/${id}`).then(
      () => {
        getData()
      }
    )
  }

  return (
    <div>
      <Table singleLine>
        <Table.Header>
          <Table.Row>
            <Table.HeaderCell>name</Table.HeaderCell>
            <Table.HeaderCell>surname</Table.HeaderCell>
            <Table.HeaderCell>checked</Table.HeaderCell>
            <Table.HeaderCell>update</Table.HeaderCell>
            <Table.HeaderCell>delete</Table.HeaderCell>
          </Table.Row>
        </Table.Header>
        <Table.Body>
          {
            APIData.map(
              (data) => {
                return (
                  <Table.Row>
                    <Table.Cell>{data.name}</Table.Cell>
                    <Table.Cell>{data.surname}</Table.Cell>
                    <Table.Cell>{data.checked ? 'Checked' : 'Unchecked'}</Table.Cell>
                    <Link to='/update'>
                      <Table.Cell>
                        <Button onClick={() => setData(data)}>update</Button>
                      </Table.Cell>
                    </Link>
                    <Link>
                      <Table.Cell>
                        <Button onClick={() => onDelete(data.id)}>delete</Button>
                      </Table.Cell>
                    </Link>
                  </Table.Row>
                )
              }
            )
          }
        </Table.Body>
      </Table>
    </div>
  )
}

export default Read
