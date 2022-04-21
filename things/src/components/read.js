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
}
