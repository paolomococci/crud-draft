import axios from 'axios'
import {
  React,
  useState
} from 'react'
import {
  Form,
  Button,
  Checkbox
} from 'semantic-ui-react'
import {
  useHistory
} from 'react-router'

function Create() {
  let history = useHistory()
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
}
