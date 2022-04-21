import axios from 'axios'
import {
  React,
  useState,
  useEffect
} from 'react'
import {
  Form,
  Button,
  Checkbox
} from 'semantic-ui-react'

import {
  useHistory
} from 'react-router'

function Update() {

  let history = useHistory()

  const [
    id,
    setId
  ] = useState(null)

  const [
    name,
    setName
  ] = useState('')

  const [
    surname,
    setSurname
  ] = useState('')

  const [
    checked,
    setChecked
  ] = useState(false)

  useEffect(
    () => {
      setId(localStorage.getItem('ID'))
      setName(localStorage.getItem('Name'))
      setSurname(localStorage.getItem('Surname'))
      setChecked(localStorage.getItem('Checked'))
    }, []
  )

  const updateAPIData = () => {
    axios.put(
      `http://localhost/sampledata`,
      {
        name,
        surname,
        checked
      }
    ).then(
      () => {
        history.push('/read')
      }
    )
  }
  
}
