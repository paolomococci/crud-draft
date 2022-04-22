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
  useNavigate
} from 'react-router-dom'

function Update() {

  let history = useNavigate()

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
      `http://localhost/sampledata/${id}`,
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

  return (
    <div>
    <Form className="create-form">
    <Form.Field>
      <label>name</label>
      <input placeholder="name" onChange={(e) => setName(e.target.value)} />
    </Form.Field>
    <Form.Field>
      <label>surname</label>
      <input placeholder="surname" onChange={(e) => setSurname(e.target.value)} />
    </Form.Field>
    <Form.Field>
      <Checkbox label="accept terms and conditions" onChange={() => setChecked(!checked)} />
    </Form.Field>
    <Button onClick={updateAPIData} type="submit">update</Button>
    </Form>
    </div>
  )
}

export default Update
