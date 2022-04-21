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
}
