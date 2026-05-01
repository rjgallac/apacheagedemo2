export default function DeptForm() {
    return (
        <div>
            <h4>Add Department</h4>

            <form>
                <label>Department Name:
                    <input type="text" name="deptName" />
                </label>
                <button type="submit" style={{width: 'stretch', backgroundColor: 'seagreen', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Add Department</button>
            </form>

            <h4>link department to company</h4>

            <h4>link department to person</h4>
        </div>
    );
}